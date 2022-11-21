package com.nima.common.mappers

import com.nima.common.model.ConfigBody
import com.nima.common.model.ResponseClass
import com.nima.common.model.UrlIdFirst
import com.nima.common.model.UrlIdSecond
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser

fun ResponseClass.mapToProperModel(result: String): ResponseClass? {
    return try {
        when (this) {
            is ConfigBody -> result.toConfigBodyMapper()
        }
    } catch (e: Exception) {
        null
    }
}


fun String.toConfigBodyMapper(): ConfigBody {
    val jsonObject = JSONParser().parse(this) as JSONObject
    val validUrlsJson = jsonObject["validRequestUrls"] as JSONArray
    val urlIdFirstJson = jsonObject["urlIdFirst"] as JSONArray
    val urlIdSecondJson = jsonObject["urlIdSecond"] as JSONArray
    val validUrls = ArrayList<String>()
    val urlIdFirst = ArrayList<UrlIdFirst>()
    val urlIdSecond = ArrayList<UrlIdSecond>()
    validUrlsJson.forEach { validUrls.add(it as String) }
    urlIdFirstJson.forEach { urlIdFirst.add(UrlIdFirst(it as Long)) }
    urlIdSecondJson.forEach { urlIdSecond.add(UrlIdSecond(it as Long)) }
    return ConfigBody(
        validRequestUrls = validUrls,
        urlIdFirst = urlIdFirst,
        urlIdSecond = urlIdSecond,
        isLive = jsonObject["isLive"] as Boolean,
        syncType = jsonObject["syncType"] as String
    )
}