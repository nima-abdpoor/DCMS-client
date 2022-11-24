package com.nima.common.database.entitty

import androidx.room.Entity
import androidx.room.PrimaryKey

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