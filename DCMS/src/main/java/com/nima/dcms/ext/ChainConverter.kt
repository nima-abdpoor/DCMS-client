package com.nima.dcms.ext

import com.nima.common.model.DCMSResponseBody
import okhttp3.Headers

fun DCMSResponseBody.getFormattedRequestString(time: String): String {
    return "\"request\":{\"url\":\"${url}\",\"body\":\"${body}\",\"header\":\"${headers}\",\"requestTime\":\"${time}\"},"
}

fun DCMSResponseBody.getFormattedResponseString(): String {
    return "\"response\":{\"body\":\"${"body"}\",\"code\":\"${code}\",\"header\":${headers}\"requestTime\":\"${time}\"}"
}


fun String.cleanURL(): String {
    return if (this[lastIndex] != '/') "$this/" else this
}

fun Headers.toJsonFormat(): String {
    val result = StringBuilder()
    var i = 0
    val size: Int = size()
    result.append("{")
    while (i < size) {
        result.append("\"").append(name(i)).append("\"").append(":\"").append(value(i))
            .append("\",")
        i++
    }
    val finalHeader = if (size > 0)
        result.replace(result.length - 2, result.length - 1, "\"}")
    else result.append("}")
    return finalHeader.toString()
}