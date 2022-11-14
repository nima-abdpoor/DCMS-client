package com.nima.dcms.datasource.abstraction

import com.nima.dcms.datasource.database.entitty.URLIdFirst

interface MyDaoService {
    suspend fun getAllUrls() : List<URLIdFirst>
    suspend fun insertURL(urlIdFirst: URLIdFirst)
}