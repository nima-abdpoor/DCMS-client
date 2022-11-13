package com.nima.dcms.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nima.dcms.database.entitty.Config
import com.nima.dcms.database.entitty.URLIdFirst
import com.nima.dcms.database.entitty.URLIdSecond


@Database(
    entities = [
        URLIdFirst::class,
        URLIdSecond::class,
        Config::class,
    ], version = 1
)
abstract class DCMSDataBase : RoomDatabase() {
    abstract fun myDao(): MyDao
}