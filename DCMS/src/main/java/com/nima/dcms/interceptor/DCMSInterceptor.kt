package com.nima.dcms.interceptor

import android.content.Context
import com.nima.common.abstraction.MyDaoService
import com.nima.common.database.entitty.Regex
import com.nima.common.database.entitty.URLIdSecond
import com.nima.common.database.getDao
import com.nima.common.implementation.MyDaoServiceImpl
import com.nima.dcms.urlconverter.CR32URLConverter
import com.nima.dcms.urlconverter.URLConverter
import kotlinx.coroutines.*
import okhttp3.Interceptor
import okhttp3.Response

class DCMSInterceptor(context: Context) : Interceptor {
    private lateinit var deferredUrlFirst: Deferred<List<Long>>
    private lateinit var deferredUrlSecond: Deferred<List<URLIdSecond>>
    private lateinit var deferredRegex: Deferred<List<Regex>>
    private val finder = DCMSUrlFinder()
    private lateinit var dbService: MyDaoService
    private var urlHashFirst: List<Long>? = null
    private var urlHashSecond: List<URLIdSecond>? = null
    private var regexes: List<Regex>? = null
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


    private fun isUrlExists(url: String): Boolean {
        val hash = converter.convert(url)
        urlHashFirst?.let {
            return if (finder.searchInUrlFirst(hash, it)) true
            else finder.searchInUrlSecond(url, hash, urlHashSecond, regexes)
        } ?: kotlin.run {
            return finder.searchInUrlSecond(url, hash, urlHashSecond, regexes)
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