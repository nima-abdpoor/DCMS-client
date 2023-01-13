package com.nima.dcms.interceptor

import android.content.Context
import com.nima.common.abstraction.MyDaoService
import com.nima.common.database.entitty.Config
import com.nima.common.database.entitty.Regex
import com.nima.common.database.entitty.URLIdSecond
import com.nima.common.database.getDao
import com.nima.common.database.sharedpref.SharedPreferencesHelper
import com.nima.common.file.FileManager
import com.nima.common.implementation.MyDaoServiceImpl
import com.nima.common.model.DCMSResponseBody
import com.nima.common.utils.BASE_URL
import com.nima.common.utils.SEND_LOG_URL
import com.nima.dcms.ext.cleanURL
import com.nima.dcms.ext.getFormattedRequestString
import com.nima.dcms.ext.getFormattedResponseString
import com.nima.dcms.ext.toJsonFormat
import com.nima.dcms.urlconverter.CR32URLConverter
import com.nima.dcms.urlconverter.URLConverter
import com.nima.network.manager.request.FileUploaderHttpRequestBuilder
import kotlinx.coroutines.*
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody

class DCMSInterceptor(context: Context) : Interceptor {
    private lateinit var deferredUrlFirst: Deferred<List<Long>>
    private lateinit var deferredUrlSecond: Deferred<List<URLIdSecond>>
    private lateinit var deferredRegex: Deferred<List<Regex>>
    private lateinit var deferredConfig: Deferred<Config>
    private val finder = DCMSUrlFinder()
    private var dbService: MyDaoService
    private var urlHashFirst: List<Long>? = null
    private var urlHashSecond: List<URLIdSecond>? = null
    private var regexes: List<Regex>? = null
    private var config: Config? = null
    private var converter: URLConverter
    private val fileManager = FileManager(context)
    private val pref = SharedPreferencesHelper()

    init {
        val db = getDao(context)
        dbService = MyDaoServiceImpl(db)
        fetchUrlsFromDatabase()
        converter = CR32URLConverter()
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val startTime = System.currentTimeMillis() / 1000
        val response = chain.proceed(chain.request())
        val contentType = response.body()?.contentType()
        val content = response.body()?.string()
        val finishTime = System.currentTimeMillis() / 1000
        val wrappedBody = ResponseBody.create(contentType, content ?: "")
        CoroutineScope(Dispatchers.IO).launch {
            val request = chain.request()
            if (!deferredRegex.isActive && !deferredUrlFirst.isActive && !deferredUrlSecond.isActive) {
                if (isUrlExists(request.url().toString().cleanURL())) mergeRequestAndResponse(
                    DCMSResponseBody(
                        isSuccessful = false,
                        code = "0",
                        method = request.method(),
                        url = request.url().toString(),
                        body = request.body().toString(),
                        headers = request.headers().toJsonFormat(),
                        time = startTime.toString(),
                    ),
                    DCMSResponseBody(
                        isSuccessful = response.isSuccessful,
                        code = response.code().toString(),
                        body = content ?: "",
                        headers = response.headers().toJsonFormat(),
                        time = finishTime.toString(),
                        url = "",
                        method = "",
                    ),
                    startTime.toString(),
                )?.let {
                    saveOrSendLog(it.toString())
                }
            }
        }
        return response.newBuilder().body(wrappedBody).build()
    }


    private fun isUrlExists(url: String): Boolean {
        val hash = converter.convert(url)
        urlHashFirst?.let {
            return if (finder.searchInUrlFirst(hash, it)) true
            else finder.searchInUrlSecond(url, urlHashSecond, regexes)
        } ?: kotlin.run {
            return finder.searchInUrlSecond(url, urlHashSecond, regexes)
        }
    }

    private fun fetchUrlsFromDatabase() {
        CoroutineScope(Dispatchers.IO).launch {
            deferredUrlFirst = async { dbService.getAllUrlFirst().map { it.urlHash ?: 0 } }
            deferredUrlSecond = async { dbService.getAllUrlSecond() }
            deferredRegex = async { dbService.getRegex() }
            deferredConfig = async { dbService.getConfig() }
            urlHashFirst = deferredUrlFirst.await()
            regexes = deferredRegex.await()
            urlHashSecond = deferredUrlSecond.await()
            config = deferredConfig.await()
        }
    }

    private fun saveUrl(log: String) {
        fileManager.writeIntoFile(log + "\n")
    }

    private fun mergeRequestAndResponse(
        request: DCMSResponseBody,
        response: DCMSResponseBody,
        startTime: String
    ): java.lang.StringBuilder? {
        config?.let { it ->
            val sb = StringBuilder("{")
            if ((it.saveError && !response.isSuccessful) || (it.saveSuccess && response.isSuccessful)) {
                if (it.saveRequest) sb.append(request.getFormattedRequestString(startTime))
                if (it.saveResponse) sb.append(response.getFormattedResponseString())
            }
            return sb.replace(sb.length - 2, sb.length - 1, "\"}")
        }
        return null
    }

    private fun saveOrSendLog(
        log: String
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            if (config?.isLive == true) {
                if (!sendLogToServer(log)) saveUrl(log)
            } else
                saveUrl(log)
        }
    }

    private fun sendLogToServer(log: String) = runBlocking {
        try {
            val request =
                FileUploaderHttpRequestBuilder(BASE_URL + SEND_LOG_URL + pref.getUniqueId())
            request.addFormField("", log)
            request.addFormField("Content-Length", log.length.toString())
            request.finish()
            return@runBlocking true
        } catch (e: Exception) {
            return@runBlocking false
        }
    }
}