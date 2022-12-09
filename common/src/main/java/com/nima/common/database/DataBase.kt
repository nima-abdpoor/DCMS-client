package com.nima.common.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nima.common.database.entitty.*


@Database(
    entities = [
        URLIdFirst::class,
        URLIdSecond::class,
        Config::class,
        RequestUrl::class,
        Regex::class
    ], version = 1
)
abstract class DCMSDataBase : RoomDatabase() {
    abstract fun myDao(): MyDao

    companion object {
        const val DATABASE_NAME = "DCMS_DB"

    }
}

fun getDataBase(context: Context): DCMSDataBase {
    return Room
        .databaseBuilder(context, DCMSDataBase::class.java, DCMSDataBase.DATABASE_NAME)
        .fallbackToDestructiveMigration()
        .build()
}

fun getDao(context: Context): MyDao {
    return getDataBase(context).myDao()
}