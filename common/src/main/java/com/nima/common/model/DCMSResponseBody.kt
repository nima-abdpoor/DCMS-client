package com.nima.common.model

data class DCMSResponseBody(
    val isSuccessful:Boolean,
    val code:String,
    val body: String,
    val headers: String,
    val time: String,
)