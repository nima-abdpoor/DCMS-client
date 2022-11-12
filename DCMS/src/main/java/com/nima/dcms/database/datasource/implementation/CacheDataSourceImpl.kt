package com.nima.dcms.database.datasource.implementation

import com.nima.dcms.database.datasource.abstract.CacheDataSourcee

//import com.nima.dcms.database.datasource.abstract.CacheDataSourcee
//import com.nima.dcms.database.entitty.URLIdsEntity
//import io.objectbox.BoxStore

class CacheDDImpl() : CacheDataSourcee {

    override fun getAllURLIds(): List<String> {
        return listOf("asdfkl")
    }
}