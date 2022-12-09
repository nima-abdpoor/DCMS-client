package com.nima.common.abstraction

import com.nima.common.database.entitty.*

interface MyDaoService {
    suspend fun getAllUrlFirst(): List<URLIdFirst>
    suspend fun insertURLFirst(urlIdFirst: URLIdFirst)
    suspend fun getAllRequestUrl(): List<RequestUrl>
    suspend fun insertRequestUrl(requestUrl: RequestUrl)
    suspend fun getAllUrlSecond(): List<URLIdSecond>
    suspend fun insertURLSecond(urlIdSecond: URLIdSecond)
    suspend fun getRegex(): List<Regex>
    suspend fun insertRegex(regex: Regex)
    suspend fun getConfig(): Config
    suspend fun insertConfig(config: Config)
}