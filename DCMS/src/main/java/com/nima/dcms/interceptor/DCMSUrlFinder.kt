package com.nima.dcms.interceptor

import com.nima.common.database.entitty.URLIdSecond
import com.nima.dcms.search.BinarySearch
import com.nima.dcms.urlconverter.CR32URLConverter

class DCMSUrlFinder {
    private val search = BinarySearch()
    private var converter = CR32URLConverter()

    fun searchInUrlFirst(hash: Long, firstUlrs: List<Long>): Boolean {
        return firstUlrs.any { it == hash }
    }


    fun searchInUrlSecond(
        url: String,
        urlHashSecond: List<URLIdSecond>?,
        regexes: List<com.nima.common.database.entitty.Regex>?
    ): Boolean {
        val urls = urlHashSecond?.map { it.id }
        urls?.forEachIndexed { index, id ->
            var acceptedRegexes = 0
            val regexesForUrl = regexes?.filter { it -> it.urlId == id }
            regexesForUrl?.get(0)?.let { regex ->
                if (regex.startIndex != null && regex.finishIndex != null && regex.regex != null) {
                    val finishIndex =
                        (url.substring(regex.startIndex!! + 1).indexOfFirst { it == '/' })
                    val changedUrl =
                        url.subSequence(
                            regex.startIndex!! + 1,
                            finishIndex + regex.startIndex!! + 1
                        )
                    if (changedUrl.matches(Regex(regex.regex!!))) {
                        acceptedRegexes++
                    }
                    val newUrl = url.replaceRange(
                        regex.startIndex!! + 1,
                        finishIndex + regex.startIndex!! + 1,
                        "*"
                    )
                    val hash = converter.convert(newUrl)
                    println("newUrl:$newUrl+ ${converter.convert(newUrl)}")
                    val hashSecond = urlHashSecond[index].urlHash
                    return hash == hashSecond
                } else return false
            } ?: kotlin.run {
                return false
            }
        }
        return false
    }
}