package com.nima.network.manager.request

import com.nima.network.manager.model.HttpMethods
import com.nima.common.model.ResponseClass
import com.nima.network.manager.util.CONNECT_TIME_OUT
import com.nima.network.manager.util.READ_TIME_OUT
import com.nima.network.manager.wrapper.Response
import com.nima.network.manager.wrapper.ResultWrapper

interface HttpRequestBuilderInterface {
    fun setMethod(method: HttpMethods): HttpRequestBuilderInterface
    fun setUrl(url: String): HttpRequestBuilderInterface
    fun setReadTimeOut(value: Int = READ_TIME_OUT): HttpRequestBuilderInterface
    fun setConnectTimeOut(value: Int = CONNECT_TIME_OUT): HttpRequestBuilderInterface
    fun setRequestProperty(vararg pairs: Pair<String, String>): HttpRequestBuilderInterface
    fun setPostData(data: String): HttpRequestBuilderInterface
    fun <T>submit(model: ResponseClass): ResultWrapper<Response<T>>
}