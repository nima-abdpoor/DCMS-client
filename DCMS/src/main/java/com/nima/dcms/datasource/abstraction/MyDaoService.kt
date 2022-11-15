package com.nima.dcms.datasource.abstraction

import com.nima.dcms.datasource.database.entitty.Config
import com.nima.dcms.datasource.database.entitty.URLIdFirst
import com.nima.dcms.datasource.database.entitty.URLIdSecond

interface MyDaoService {
    suspend fun getAllUrlFirst(): List<URLIdFirst>
    suspend fun insertURLFirst(urlIdFirst: URLIdFirst)
    suspend fun getAllUrlSecond(): List<URLIdSecond>
    suspend fun insertURLSecond(urlIdSecond: URLIdSecond)
    suspend fun getConfig() : Config
    suspend fun insertConfig(config: Config)
}