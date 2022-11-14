package com.nima.dcms.datasource.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nima.dcms.datasource.database.entitty.Config
import com.nima.dcms.datasource.database.entitty.URLIdFirst
import com.nima.dcms.datasource.database.entitty.URLIdSecond

@Dao
interface MyDao {

    //URLIdFirst
    @Query("SELECT * FROM URLIdFirst")
    fun getURLIdFirst(): List<URLIdFirst>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertURLIdFirst(vararg ids: URLIdFirst)

    //URLIdSecond
    @Query("SELECT * FROM URLIdFirst")
    fun getURLIdSecond(): URLIdSecond

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertURLIdSecond(vararg ids: URLIdSecond)

    //Config
    @Query("SELECT * FROM Config")
    fun getConfig(): Config

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertConfig(vararg configs: Config)
}