package com.nima.common.implementation

import com.nima.common.abstraction.MyDaoService
import com.nima.common.database.MyDao
import com.nima.common.database.entitty.Config
import com.nima.common.database.entitty.RequestUrl
import com.nima.common.database.entitty.URLIdFirst
import com.nima.common.database.entitty.URLIdSecond

class MyDaoServiceImpl(
    private val dao: MyDao,
) : MyDaoService {

    override suspend fun getAllUrlFirst(): List<URLIdFirst> {
        return dao.getURLIdFirst()
    }

    override suspend fun insertURLFirst(urlIdFirst: URLIdFirst) {
        dao.insertURLIdFirst(urlIdFirst)
    }

    override suspend fun getAllRequestUrl(): List<RequestUrl> {
        return dao.getRequestUrl()
    }

    override suspend fun insertRequestUrl(requestUrl: RequestUrl) {
        dao.insertRequestUrl(requestUrl)
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