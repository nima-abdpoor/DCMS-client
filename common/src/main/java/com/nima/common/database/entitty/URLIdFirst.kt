package com.nima.common.database.entitty

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class URLIdFirst(
    @PrimaryKey var id: Long? = 0,
    var urlHash: Long? = null
)