package com.nima.dcms.interceptor

import com.nima.common.database.entitty.URLIdSecond
import com.nima.dcms.search.BinarySearch

class DCMSUrlFinder {
    private val search = BinarySearch()

    fun searchInUrlFirst(hash: Long, firstUlrs: List<Long>): Boolean {
        return firstUlrs.any { it == hash }
    }


    fun searchInUrlSecond(
        url: String,
        hash: Long,
        urlHashSecond: List<URLIdSecond>?,
        regexes: List<com.nima.common.database.entitty.Regex>?
    ): Boolean {
        val urls = urlHashSecond?.map { it.id }
        urls?.forEachIndexed { index, id ->
            var acceptedRegexes = 0
            val regexesForUrl = regexes?.filter { it -> it.urlId == id }
            regexesForUrl?.forEach { regex ->
                if (regex.startIndex != null && regex.finishIndex != null && regex.regex != null) {
                    val changedUrl =
                        url.subSequence(regex.startIndex!!, regex.finishIndex!!)
                    if (changedUrl.matches(Regex(regex.regex!!))) {
                        acceptedRegexes++
                    }
                }
            }
            if (acceptedRegexes == regexesForUrl?.size && hash == urlHashSecond[index].urlHash) return true
        }
        return false
    }
}