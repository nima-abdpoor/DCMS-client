package com.nima.dcms.interceptor

import android.content.Context
import android.util.Log
import com.nima.common.abstraction.MyDaoService
import com.nima.common.database.entitty.Config
import com.nima.common.database.entitty.Regex
import com.nima.common.database.entitty.URLIdSecond
import com.nima.common.database.getDao
import com.nima.common.implementation.MyDaoServiceImpl
import com.nima.common.model.DCMSResponseBody
import com.nima.dcms.ext.cleanURL
import com.nima.dcms.ext.getFormattedRequestString
import com.nima.dcms.ext.getFormattedResponseString
import com.nima.dcms.urlconverter.CR32URLConverter
import com.nima.dcms.urlconverter.URLConverter
import kotlinx.coroutines.*
import okhttp3.Interceptor
import okhttp3.Request
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

    init {
        val db = getDao(context)
        dbService = MyDaoServiceImpl(db)
        fetchUrlsFromDatabase()
        converter = CR32URLConverter()
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val startTime = System.currentTimeMillis()
        val response = chain.proceed(chain.request())
        val finishTime = System.currentTimeMillis()
        val contentType = response.body()?.contentType()
        val content = response.body()?.string()
        val wrappedBody = ResponseBody.create(contentType, content ?: "")
        CoroutineScope(Dispatchers.IO).launch {
            if (!deferredRegex.isActive && !deferredUrlFirst.isActive && !deferredUrlSecond.isActive) {
                if (isUrlExists(chain.request().url().toString().cleanURL())) saveUrl(
                    chain.request(),
                    DCMSResponseBody(
                        isSuccessful = response.isSuccessful,
                        code = response.code().toString(),
                        body = content ?: "",
                        headers = response.headers().toString(),
                        time = finishTime.toString()),
                    startTime,
                )
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

    private fun saveUrl(request: Request, result: DCMSResponseBody, startTime: Long) {
        config?.let { it ->
            val sb = StringBuilder("{")
            if ((it.saveError && !result.isSuccessful) || (it.saveSuccess && result.isSuccessful)) {
                if (it.saveRequest) sb.append(request.getFormattedRequestString(startTime.toString()))
                if (it.saveResponse) sb.append(result.getFormattedResponseString())
            }
            sb.replace(sb.length - 2, sb.length - 1, "}")
            Log.d("TAG", "saadfveUrl: $sb")
        }
    }

}