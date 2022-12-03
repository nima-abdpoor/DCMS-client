package com.nima.common.database.entitty

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RequestUrl(
    @PrimaryKey var id: Long? = 0,
    var url: String? = null,
)