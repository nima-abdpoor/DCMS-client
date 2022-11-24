package com.nima.common.database.entitty

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nima.common.model.ConfigBody
import com.nima.common.utils.BASE_URL

@Entity
data class Config(
    @PrimaryKey var id: Long = 0,
    var validRequestUrls: String? = null,
    var isLive: Boolean? = false,
    var syncType: NetWorkSyncType? = NetWorkSyncType.Default,
)

enum class NetWorkSyncType(value: String) {
    Default("0"), Low("1"), Medium("2"), High("3")
}

fun String.toNetWorkSyncType(): NetWorkSyncType {
    return when (this) {
        "1" -> NetWorkSyncType.Low
        "2" -> NetWorkSyncType.Medium
        "3" -> NetWorkSyncType.High
        else -> NetWorkSyncType.Default
    }
}

fun ConfigBody?.toConfig(): Config {
    var requestUrls = ""
    this?.let {
        validRequestUrls?.forEach {
            requestUrls += "$it,"
        }
        return Config(
            id = 0,
            validRequestUrls = requestUrls.removeSuffix(","),
            isLive = isLive,
            syncType = syncType?.toNetWorkSyncType() ?: NetWorkSyncType.Default
        )
    } ?: kotlin.run {
        return Config(
            id = 0,
            validRequestUrls = BASE_URL,
            isLive = false,
            syncType = NetWorkSyncType.Default
        )
    }
}