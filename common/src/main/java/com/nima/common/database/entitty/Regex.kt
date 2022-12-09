package com.nima.common.database.entitty

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Regex(
    @PrimaryKey var id: Long? = 0,
    var urlId: Long? = 0,
    var regex: String? = null,
    var startIndex: Long? = null,
    var finishIndex: Long? = null,
)