package com.nima.dcms.datasource.implementation

import com.nima.dcms.datasource.abstraction.MyDaoService
import com.nima.dcms.datasource.database.MyDao
import com.nima.dcms.datasource.database.entitty.Config
import com.nima.dcms.datasource.database.entitty.URLIdFirst
import com.nima.dcms.datasource.database.entitty.URLIdSecond

class MyDaoServiceImpl(
    private val dao: MyDao,
) : MyDaoService {

    override suspend fun getAllUrlFirst(): List<URLIdFirst> {
        return dao.getURLIdFirst()
    }

    override suspend fun insertURLFirst(urlIdFirst: URLIdFirst) {
        dao.insertURLIdFirst(urlIdFirst)
    }

    override suspend fun getAllUrlSecond(): List<URLIdSecond> {
        return dao.getURLIdSecond()
    }

    override suspend fun insertURLSecond(urlIdSecond: URLIdSecond) {
        dao.insertURLIdSecond(urlIdSecond)
    }

    override suspend fun getConfig(): Config {
        return dao.getConfig()
    }

    override suspend fun insertConfig(config: Config) {
        dao.insertConfig(config)
    }
}