package com.nima.dcms.ext

import okhttp3.Request
import okhttp3.Response

fun Request.getFormattedRequestString(time: String): String {
    return "{\"request\":\"{\"url\":\"${url()},\"body\":\"${body()}\",\"header\":\"${headers()}\",\"requestTime\":\"${time}\"},"
}

fun Response.getFormattedResponseString(time: String): String {
    return "{\"response\":\"{\"body\":\"${body()}\",\"header\":\"${headers()}\",\"requestTime\":\"${time}\"},"
}