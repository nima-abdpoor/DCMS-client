package com.nima.common.database.entitty

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nima.common.model.ConfigBody
import com.nima.common.utils.DEFAULT_REPEAT_INTERVAL_WORKER_TIME
import com.nima.common.utils.DEFAULT_REPEAT_INTERVAL_WORK_TIME_UNIT

@Entity
data class Config(
    @PrimaryKey var id: Long = 0,
    var isLive: Boolean? = false,
    var saveRequest: Boolean = false,
    var saveResponse: Boolean = false,
    var saveError: Boolean = true,
    var saveSuccess: Boolean = false,
    var netWorkType: String? = "6",
    var repeatInterval: Long? = DEFAULT_REPEAT_INTERVAL_WORKER_TIME,
    var repeatIntervalTimeUnit: String? = DEFAULT_REPEAT_INTERVAL_WORK_TIME_UNIT,
    var requiresBatteryNotLow: Boolean = true,
    var requiresStorageNotLow: Boolean = true,
    var requiresCharging: Boolean = false,
    var requiresDeviceIdl: Boolean = false,

)

fun ConfigBody?.toConfig(): Config {
    var requestUrls = ""
    this?.let {
        validRequestUrls?.forEach {
            requestUrls += "$it,"
        }
        return Config(
            id = 0,
            isLive = isLive,
            netWorkType = netWorkType,
            saveRequest = saveRequest,
            saveResponse = saveResponse,
            saveError = saveError,
            saveSuccess = saveSuccess,
            repeatInterval = repeatInterval,
            repeatIntervalTimeUnit = repeatIntervalTimeUnit,
            requiresBatteryNotLow = requiresBatteryNotLow,
            requiresStorageNotLow = requiresStorageNotLow,
            requiresCharging = requiresCharging,
            requiresDeviceIdl = requiresDeviceIdl,
        )
    } ?: kotlin.run {
        return Config(
            id = 0,
            isLive = false,
            netWorkType = "6"
        )
    }
}