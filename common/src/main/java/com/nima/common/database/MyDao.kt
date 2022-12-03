package com.nima.common.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nima.common.database.entitty.Config
import com.nima.common.database.entitty.RequestUrl
import com.nima.common.database.entitty.URLIdFirst
import com.nima.common.database.entitty.URLIdSecond

@Dao
interface MyDao {

    //URLIdFirst
    @Query("SELECT * FROM URLIdFirst")
    fun getURLIdFirst(): List<URLIdFirst>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertURLIdFirst(vararg ids: URLIdFirst)

    //RequestUrl
    @Query("SELECT * FROM RequestUrl")
    fun getRequestUrl(): List<RequestUrl>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRequestUrl(vararg ids: RequestUrl)

    //URLIdSecond
    @Query("SELECT * FROM URLIdSecond")
    fun getURLIdSecond(): List<URLIdSecond>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertURLIdSecond(vararg ids: URLIdSecond)

    //Config
    @Query("SELECT * FROM Config")
    fun getConfig(): Config

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertConfig(vararg configs: Config)
}