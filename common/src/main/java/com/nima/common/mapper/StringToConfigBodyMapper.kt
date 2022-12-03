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


fun <T : ResponseClass> String.toConfigBodyMapper(): T {
    val jsonObject = JSONParser().parse(this) as JSONObject
    val configJson = jsonObject["config"] as JSONObject
    val validUrlsJson = jsonObject["requestUrl"] as JSONArray
    val urlIdFirstJson = jsonObject["urlFirst"] as JSONArray
    val urlIdSecondJson = jsonObject["urlSecond"] as JSONArray
    val validUrls = ArrayList<String>()
    val urlIdFirst = ArrayList<UrlIdFirst>()
    val urlIdSecond = ArrayList<UrlIdSecond>()
    validUrlsJson.forEach {
        it as JSONObject
        validUrls.add(it["request_url"] as String)
    }
    urlIdFirstJson.forEach {
        it as JSONObject
        urlIdFirst.add(UrlIdFirst(id = (it["url_hash"] as String).toLong()))
    }
    urlIdSecondJson.forEach {
        it as JSONObject
        urlIdSecond.add(
            UrlIdSecond(
                id = (it["url_hash"] as String).toLong(),
                regex = it["regex"] as String,
                startIndex = it["start_index"] as Long,
                finishIndex = it["finish_index"] as Long,
            )
        )
    }
    return ConfigBody(
        validRequestUrls = validUrls,
        urlIdFirst = urlIdFirst,
        urlIdSecond = urlIdSecond,
        isLive = configJson["is_live"] as Boolean,
        syncType = configJson["sync_type"] as String
    ) as T
}