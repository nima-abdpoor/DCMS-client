package com.nima.dcms.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class DCMSInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(chain.request())
    }
}