package com.nima.dcms.datasource.implementation

import com.nima.dcms.datasource.abstraction.MyDaoService
import com.nima.dcms.datasource.database.MyDao
import com.nima.dcms.datasource.database.entitty.URLIdFirst

class MyDaoServiceImpl(
    private val dao: MyDao,
): MyDaoService {

    override suspend fun getAllUrls(): List<URLIdFirst> {
        return dao.getURLIdFirst()
    }

    override suspend fun insertURL(urlIdFirst: URLIdFirst) {
        dao.insertURLIdFirst(urlIdFirst)
    }
}