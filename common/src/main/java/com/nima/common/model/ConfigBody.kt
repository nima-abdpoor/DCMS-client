package com.nima.common.model

import com.nima.common.utils.DEFAULT_REPEAT_INTERVAL_WORKER_TIME
import com.nima.common.utils.DEFAULT_REPEAT_INTERVAL_WORK_TIME_UNIT

sealed class ResponseClass

data class ConfigBody(
    val id : Long? = null,
    val validRequestUrls: List<String>? = null,
    val urlIdFirst: List<UrlIdFirst>? = null,
    val urlIdSecond: List<UrlIdSecond>? = null,
    val isLive: Boolean? = false,
    var saveRequest: Boolean = false,
    var saveResponse: Boolean = false,
    var saveError: Boolean = true,
    var saveSuccess: Boolean = true,
    var netWorkType: String? = "6",
    var repeatInterval: Long = DEFAULT_REPEAT_INTERVAL_WORKER_TIME,
    var repeatIntervalTimeUnit: String = DEFAULT_REPEAT_INTERVAL_WORK_TIME_UNIT,
    var requiresBatteryNotLow: Boolean = true,
    var requiresStorageNotLow: Boolean = true,
    var requiresCharging: Boolean = false,
    var requiresDeviceIdl: Boolean = false,
) : ResponseClass()