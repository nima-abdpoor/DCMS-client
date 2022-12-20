package com.nima.common.model

sealed class ResponseClass

data class ConfigBody(
    val id : Long? = null,
    val validRequestUrls: List<String>? = null,
    val urlIdFirst: List<UrlIdFirst>? = null,
    val urlIdSecond: List<UrlIdSecond>? = null,
    val isLive: Boolean? = false,
    var syncType: String? = "0",
    var saveRequest: Boolean = false,
    var saveResponse: Boolean = false,
    var saveError: Boolean = true,
    var saveSuccess: Boolean = true,
) : ResponseClass()