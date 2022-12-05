package com.nima.dcms.interceptor

import com.nima.common.abstraction.MyDaoService
import com.nima.common.database.getDao
import com.nima.common.implementation.MyDaoServiceImpl
import com.nima.dcms.base.BaseDCMS
import okhttp3.Interceptor
import okhttp3.Response

class DCMSInterceptor : Interceptor, BaseDCMS() {
    private lateinit var dbService: MyDaoService

    override fun intercept(chain: Interceptor.Chain): Response {
        val db = getDao(getContext())
        dbService = MyDaoServiceImpl(db)
        return chain.proceed(chain.request())
    }

}