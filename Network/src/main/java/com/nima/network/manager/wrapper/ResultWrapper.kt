package com.nima.network.manager.wrapper

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T?): ResultWrapper<T>()
    data class GenericError(val error: ErrorResponse? = null): ResultWrapper<Nothing>()
    data class NetworkError(val error: ErrorResponse? = null) : ResultWrapper<Nothing>()
}

data class ErrorResponse(
    val cause: String? = "",
    val message: String? = "",
    val url: String? = "",
    val code: Int? = 0
)
