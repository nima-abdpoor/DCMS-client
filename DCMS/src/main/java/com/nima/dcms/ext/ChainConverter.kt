package com.nima.dcms.ext

import com.nima.common.model.DCMSResponseBody
import okhttp3.Request

fun Request.getFormattedRequestString(time: String): String {
    return "{\"request\":\"{\"url\":\"${url()},\"body\":\"${body()}\",\"header\":\"${headers()}\",\"requestTime\":\"${time}\"},"
}

fun DCMSResponseBody.getFormattedResponseString(): String {
    return "{\"response\":\"{\"body\":\"${body}\",\"header\":\"${headers}\",\"requestTime\":\"${time}\"},"
}


fun String.cleanURL(): String{
    return if (this[lastIndex] != '/')  "$this/" else this
}