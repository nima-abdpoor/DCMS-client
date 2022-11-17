package com.nima.network.manager.model

sealed class ResponseClass

data class ConfigBody(
    val validRequestUrls: List<String>? = null,
    val isLive: Boolean? = false,
    var syncType: String? = "0"
) : ResponseClass()