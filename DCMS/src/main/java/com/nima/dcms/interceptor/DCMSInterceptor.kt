package com.nima.dcms.interceptor

import android.content.Context
import com.nima.common.abstraction.MyDaoService
import com.nima.common.database.getDao
import com.nima.common.implementation.MyDaoServiceImpl
import okhttp3.Interceptor
import okhttp3.Response

class DCMSInterceptor(private val context: Context) : Interceptor {
    private lateinit var dbService: MyDaoService

    override fun intercept(chain: Interceptor.Chain): Response {
        val db = getDao(context)
        dbService = MyDaoServiceImpl(db)
        return chain.proceed(chain.request())
    }

}