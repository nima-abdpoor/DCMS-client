package com.nima.dcms.interceptor

import android.content.Context
import android.util.Log
import com.nima.common.abstraction.MyDaoService
import com.nima.common.database.entitty.URLIdSecond
import com.nima.common.database.getDao
import com.nima.common.implementation.MyDaoServiceImpl
import com.nima.dcms.search.BinarySearch
import com.nima.dcms.urlconverter.CR32URLConverter
import com.nima.dcms.urlconverter.URLConverter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Response

class DCMSInterceptor(context: Context) : Interceptor {
    private lateinit var dbService: MyDaoService
    private var urlHashFirst: List<Long>? = null
    private var urlHashSecond: List<URLIdSecond>? = null
    private val search = BinarySearch()
    private var converter : URLConverter

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
        arr?.forEach {
            Log.d("TAG", "intercept: array: $it hash: $hash")
        }
        val index = search.findIdFromArray(arr!!, hash)
        Log.d("TAG", "intercept: index $index")
        if (index >= 0){
            Log.d("TAG", "intercept:  value: ${arr[index]}")
        }
        return chain.proceed(chain.request())
    }

    private fun fetchUrlsFromDatabase() {
        CoroutineScope(Dispatchers.IO).launch {
            launch {
                urlHashFirst = dbService.getAllUrlFirst().map { it.urlHash ?: 0 }
            }
            launch {
                urlHashSecond = dbService.getAllUrlSecond()
            }
        }
    }

}