package com.nima.common.mapper

import com.nima.common.model.*
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser

fun <T : ResponseClass> ResponseClass.mapToProperModel(result: String): T? {
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
        val regexes = ArrayList<Regex>()
        it as JSONObject
        val regexArray = it["Regex"] as JSONArray
        regexArray.forEach { regexJsonObject ->
            regexJsonObject as JSONObject
            regexes.add(
                Regex(
                    regex = regexJsonObject["regex"] as String,
                    startIndex = (regexJsonObject["start_index"] as Long).toInt(),
                    finishIndex = (regexJsonObject["finish_index"] as Long).toInt(),
                )
            )
        }
        urlIdSecond.add(
            UrlIdSecond(
                id = (it["UrlHash"] as String).toLong(),
                regex = regexes
            )
        )
    }
    return ConfigBody(
        validRequestUrls = validUrls,
        urlIdFirst = urlIdFirst,
        urlIdSecond = urlIdSecond,
        isLive = configJson["is_live"] as Boolean,
        saveRequest = configJson["save_request"] as Boolean,
        saveResponse = configJson["save_response"] as Boolean,
        saveError = configJson["save_error"] as Boolean,
        saveSuccess = configJson["save_success"] as Boolean,
        netWorkType = configJson["network_type"] as String,
        repeatInterval = (configJson["repeat_interval"] as Int).toLong(),
        repeatIntervalTimeUnit = configJson["repeat_interval_time_unit"] as String,
        requiresBatteryNotLow = configJson["requires_battery_not_low"] as Boolean,
        requiresCharging = configJson["requires_charging"] as Boolean,
        requiresStorageNotLow = configJson["requires_storage_not_low"] as Boolean,
        requiresDeviceIdl = configJson["requires_device_idl"] as Boolean,
    ) as T
}