package com.nima.common.mapper

import com.nima.common.model.ConfigBody
import com.nima.common.model.ResponseClass
import com.nima.common.model.UrlIdFirst
import com.nima.common.model.UrlIdSecond
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser

fun <T: ResponseClass> ResponseClass.mapToProperModel(result: String): T? {
    return try {
        when (this) {
            is ConfigBody -> result.toConfigBodyMapper()
        }
    } catch (e: Exception) {
        null
    }
}


fun <T:ResponseClass> String.toConfigBodyMapper(): T {
    val jsonObject = JSONParser().parse(this) as JSONObject
    val validUrlsJson = jsonObject["validRequestUrls"] as JSONArray
    val urlIdFirstJson = jsonObject["urlIdFirst"] as JSONArray
    val urlIdSecondJson = jsonObject["urlIdSecond"] as JSONArray
    val validUrls = ArrayList<String>()
    val urlIdFirst = ArrayList<UrlIdFirst>()
    val urlIdSecond = ArrayList<UrlIdSecond>()
    validUrlsJson.forEach { validUrls.add(it as String) }
    urlIdFirstJson.forEach { urlIdFirst.add(UrlIdFirst(it as Long)) }
    urlIdSecondJson.forEach {
        it as JSONObject
        urlIdSecond.add(
            UrlIdSecond(
                ids = it["urlId"] as Long,
                regex = it["regex"] as String,
                startIndex = it["startIndex"] as Long,
                finishIndex = it["finishIndex"] as Long
            )
        )
    }
    return ConfigBody(
        validRequestUrls = validUrls,
        urlIdFirst = urlIdFirst,
        urlIdSecond = urlIdSecond,
        isLive = jsonObject["isLive"] as Boolean,
        syncType = jsonObject["syncType"] as String
    ) as T
}