package com.nima.dcms.database.datasource.abstract

//import com.nima.dcms.database.entitty.URLIdsEntity

interface CacheDataSourcee {
    fun getAllURLIds(): List<String>
}