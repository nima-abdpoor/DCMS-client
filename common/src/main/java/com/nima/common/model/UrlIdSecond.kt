package com.nima.common.model

data class UrlIdSecond(
    val id: Long? = null,
    val regex: List<Regex>
)

data class Regex(
    val regex: String? = null,
    val startIndex: Int? = null,
    val finishIndex: Int? = null,
)