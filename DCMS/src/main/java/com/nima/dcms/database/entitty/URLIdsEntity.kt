package com.nima.dcms.database.entitty

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id


@Entity
data class URLIdsEntity(
    @Id var id: Long = 0,
    var flexList: MutableList<Any>? = null,
    var urls: MutableList<Any>? = null,
)