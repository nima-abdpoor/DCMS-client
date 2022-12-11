package com.nima.dcms.interceptor

import android.content.Context
import android.util.Log
import com.nima.common.abstraction.MyDaoService
import com.nima.common.database.entitty.Regex
import com.nima.common.database.entitty.URLIdSecond
import com.nima.common.database.getDao
import com.nima.common.implementation.MyDaoServiceImpl
import com.nima.dcms.search.BinarySearch
import com.nima.dcms.urlconverter.CR32URLConverter
import com.nima.dcms.urlconverter.URLConverter
import kotlinx.coroutines.*
import okhttp3.Interceptor
import okhttp3.Response

class DCMSInterceptor(context: Context) : Interceptor {
    private lateinit var deferredUrlFirst: Deferred<List<Long>>
    private lateinit var deferredUrlSecond: Deferred<List<URLIdSecond>>
    private lateinit var deferredRegex: Deferred<List<Regex>>
    private lateinit var dbService: MyDaoService
    private var urlHashFirst: List<Long>? = null
    private var urlHashSecond: List<URLIdSecond>? = null
    private var regexes: List<Regex>? = null
    private val search = BinarySearch()
    private var converter: URLConverter

    init {
        val db = getDao(context)
        dbService = MyDaoServiceImpl(db)
        fetchUrlsFromDatabase()
        converter = CR32URLConverter()
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!deferredRegex.isActive && !deferredUrlFirst.isActive && !deferredUrlSecond.isActive) {
            isUrlExists(chain.request().url().toString())
        }
        return chain.proceed(chain.request())
    }

    fun searchInUrlFirst(hash: Long, firstUlrs: LongArray): Boolean {
        firstUlrs.forEach {
            Log.d("TAG", "intercept: array: $it hash: $hash")
        }
        val index = search.findIdFromArray(firstUlrs, hash)
        Log.d("TAG", "intercept: index $index")
        return if (index >= 0) {
            Log.d("TAG", "intercept:  value: ${firstUlrs[index]}")
            return true
        } else false
    }

    fun searchInUrlSecond(
        url: String,
        hash: Long,
        urlHashSecond: List<URLIdSecond>?
    ): Boolean {
        val urls = urlHashSecond?.map { it.id }
        urls?.forEachIndexed { index, id ->
            var acceptedRegexes = 0
            val regexesForUrl = regexes?.filter { it -> it.urlId == id }
            regexesForUrl?.forEach { regex ->
                if (regex.startIndex != null && regex.finishIndex != null && regex.regex != null) {
                    val changedUrl =
                        url.subSequence(regex.startIndex!!, regex.finishIndex!!)
                    if (changedUrl.matches(Regex(regex.regex!!))) {
                        acceptedRegexes++
                    }
                }
            }
            if (acceptedRegexes == regexesForUrl?.size && hash == urlHashSecond[index].urlHash) return true
        }
        return false
    }

    private fun isUrlExists(url: String): Boolean {
        val hash = converter.convert(url)
        urlHashFirst?.toLongArray()?.let {
            return if (searchInUrlFirst(hash, it)) true
            else searchInUrlSecond(url, hash, urlHashSecond)
        } ?: kotlin.run {
            return searchInUrlSecond(url, hash, urlHashSecond)
        }
    }

    private fun fetchUrlsFromDatabase() {
        CoroutineScope(Dispatchers.IO).launch {
            deferredUrlFirst = async { dbService.getAllUrlFirst().map { it.urlHash ?: 0 } }
            deferredUrlSecond = async { dbService.getAllUrlSecond() }
            deferredRegex = async { dbService.getRegex() }
            urlHashFirst = deferredUrlFirst.await()
            regexes = deferredRegex.await()
            urlHashSecond = deferredUrlSecond.await()
        }
    }

    private fun saveUrl() {
        // TODO:
    }

}