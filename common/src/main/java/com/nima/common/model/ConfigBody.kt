package com.nima.common.model

sealed class ResponseClass

data class ConfigBody(
    val validRequestUrls: List<String>? = null,
    val urlIdFirst: List<UrlIdFirst>? = null,
    val urlIdSecond: List<UrlIdSecond>? = null,
    val isLive: Boolean? = false,
    var syncType: String? = "0"
) : ResponseClass()