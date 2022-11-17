package com.nima.network.manager.model

sealed class ResponseClass

data class ConfigBody(
    val body : String? = null
) : ResponseClass()