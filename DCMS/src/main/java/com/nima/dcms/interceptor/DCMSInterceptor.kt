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
        val url = chain.request().url().toString()
        val arr = urlHashFirst?.toLongArray()
        val hash = converter.convert(url)
        Log.d("TAG", "intercept: $url")
        if (!deferredRegex.isActive && !deferredUrlFirst.isActive && !deferredUrlSecond.isActive) {
            arr?.forEach {
                Log.d("TAG", "intercept: array: $it hash: $hash")
            }
            arr?.let {
                val index = search.findIdFromArray(it, hash)
                Log.d("TAG", "intercept: index $index")
                if (index >= 0) {
                    Log.d("TAG", "intercept:  value: ${arr[index]}")
                } else {
                    // TODO: check urlSecond
                }
            }

        }
        return chain.proceed(chain.request())
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

}