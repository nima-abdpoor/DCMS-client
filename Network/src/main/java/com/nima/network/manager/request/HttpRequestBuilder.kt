package com.nima.network.manager.request

import com.nima.network.manager.model.HttpMethods
import java.io.BufferedOutputStream
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class HttpRequestBuilder : HttpRequestBuilderInterface {
    private lateinit var httpURLConnection: HttpURLConnection
    private var requestMethod: HttpMethods = HttpMethods.GET

    override fun setUrl(url: String): HttpRequestBuilderInterface {
        httpURLConnection = URL(url).openConnection() as HttpURLConnection
        httpURLConnection.doOutput = false
        httpRequestBuilder.setUrl(url)
        return httpRequestBuilder
    }

    override fun setMethod(method: HttpMethods): HttpRequestBuilderInterface {
        httpURLConnection.requestMethod = method.value
        requestMethod = method
        httpRequestBuilder.setMethod(method)
        return httpRequestBuilder
    }

    override fun setReadTimeOut(value: Int): HttpRequestBuilderInterface {
        httpURLConnection.readTimeout = value
        httpRequestBuilder.setReadTimeOut(value)
        return httpRequestBuilder
    }

    override fun setConnectTimeOut(value: Int): HttpRequestBuilderInterface {
        httpURLConnection.connectTimeout = value
        httpRequestBuilder.setConnectTimeOut(value)
        return httpRequestBuilder
    }

    override fun setRequestProperty(vararg pairs: Pair<String, String>): HttpRequestBuilderInterface {
        httpURLConnection.apply {
            pairs.forEach {
                setRequestProperty(it.first, it.second)
                httpRequestBuilder.setRequestProperty(Pair(it.first, it.second))
            }
        }
        return httpRequestBuilder
    }

    override fun setPostData(data: String): HttpRequestBuilderInterface {
        try {
            httpURLConnection.doOutput = true
            httpURLConnection.setChunkedStreamingMode(0)
            val outputStream: OutputStream =
                BufferedOutputStream(httpURLConnection.outputStream)
            val outputStreamWriter = OutputStreamWriter(outputStream)
            outputStreamWriter.write(data)
            outputStreamWriter.flush()
            outputStreamWriter.close()
        } catch (e: Exception) {
        }
        httpRequestBuilder.setPostData(data)
        return httpRequestBuilder
    }

    override fun submit(): HttpURLConnection {
        try {
            httpURLConnection.content
        } catch (e: Exception) {
        } finally {
            httpURLConnection.disconnect()
        }
        return httpURLConnection
    }
    companion object{
        private val httpRequestBuilder = HttpRequestBuilder()
    }
}