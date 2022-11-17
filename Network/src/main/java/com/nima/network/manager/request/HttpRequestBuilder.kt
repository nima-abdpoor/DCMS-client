package com.nima.network.manager.request

import com.nima.network.manager.model.ConfigBody
import com.nima.network.manager.model.HttpMethods
import com.nima.network.manager.model.ResponseClass
import com.nima.network.manager.util.CONNECT_TIME_OUT
import com.nima.network.manager.util.READ_TIME_OUT
import com.nima.network.manager.wrapper.ErrorResponse
import com.nima.network.manager.wrapper.Response
import com.nima.network.manager.wrapper.ResultWrapper
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

class HttpRequestBuilder : HttpRequestBuilderInterface {
    private var requestProperty: MutableList<Pair<String, String>>? = null
    private lateinit var httpURLConnection: HttpURLConnection
    private var method: HttpMethods = HttpMethods.GET
    private var requestUrl: String = ""
    private var readTimeOut = READ_TIME_OUT
    private var connectTimeOut = CONNECT_TIME_OUT
    private var postData = ""

    override fun setUrl(url: String): HttpRequestBuilderInterface {
        httpRequestBuilder.requestUrl = url
        return this
    }

    override fun setMethod(method: HttpMethods): HttpRequestBuilderInterface {
        httpRequestBuilder.method = method
        return this
    }

    override fun setReadTimeOut(value: Int): HttpRequestBuilderInterface {
        httpRequestBuilder.readTimeOut = value
        return this
    }

    override fun setConnectTimeOut(value: Int): HttpRequestBuilderInterface {
        httpRequestBuilder.connectTimeOut = value
        return this
    }

    override fun setRequestProperty(vararg pairs: Pair<String, String>): HttpRequestBuilderInterface {
        httpRequestBuilder.requestProperty = pairs.toMutableList()
        return this
    }

    override fun setPostData(data: String): HttpRequestBuilderInterface {
        httpRequestBuilder.postData = data
        return this
    }

    override fun <T> submit(model: ResponseClass): ResultWrapper<Response<T>> {
        try {
            httpURLConnection =
                URL(httpRequestBuilder.requestUrl).openConnection() as HttpURLConnection
            httpURLConnection.apply {
                doOutput = false
                requestMethod = method.value
                readTimeout = readTimeOut
                connectTimeout = connectTimeOut
                httpRequestBuilder.requestProperty?.forEach {
                    setRequestProperty(it)
                }
                when (method) {
                    HttpMethods.POST -> {
                        doOutput = true
                        setChunkedStreamingMode(0)
                        val outputStream: OutputStream =
                            BufferedOutputStream(httpURLConnection.outputStream)
                        val outputStreamWriter = OutputStreamWriter(outputStream)
                        outputStreamWriter.write(postData)
                        outputStreamWriter.flush()
                        outputStreamWriter.close()
                    }
                    else -> {}
                }
                val br: BufferedReader?
                if (responseCode in 100..399) {
                    br = BufferedReader(InputStreamReader(inputStream))
                    var strCurrentLine: String?
                    var result = ""
                    while (br.readLine().also { strCurrentLine = it } != null) {
                        result += strCurrentLine
                    }
                    val response = Response<T>()
                    response.isSuccessful = true
                    response.body = result.mapStringToModel(model)
                    return ResultWrapper.Success(response)
                } else {
                    br = BufferedReader(InputStreamReader(errorStream))
                    var strCurrentLine: String?
                    var result = ""
                    while (br.readLine().also { strCurrentLine = it } != null) {
                        result += strCurrentLine
                    }
                    return ResultWrapper.NetworkError(convertErrorBody(result))
                }
            }
        } catch (e: Exception) {
            ResultWrapper.GenericError(ErrorResponse())
        } finally {
            httpURLConnection.disconnect()
        }
        return ResultWrapper.GenericError(ErrorResponse())
    }

    private fun convertErrorBody(errorBody: String?): ErrorResponse {
        errorBody?.let {
            return try {
                val objError = JSONObject(errorBody)
                var url = ""
                val message: String? = if (objError.has("message"))
                    objError.getString("message")
                else null
                val cause: String? = if (objError.has("cause"))
                    objError.getString("cause")
                else null
                if (objError.has("url"))
                    url = objError.getString("url")
                ErrorResponse(cause, message, url)
            } catch (e: Exception) {
                ErrorResponse(message = "error accrued: ${e.message}")
            }
        } ?: kotlin.run {
            return ErrorResponse("Null Response Body")
        }

    }

    companion object {
        private val httpRequestBuilder = HttpRequestBuilder()
    }
}

private fun String?.mapStringToModel(model: ResponseClass): ResponseClass {
    when (model) {
        is ConfigBody -> return ConfigBody(body = this)
    }
}
