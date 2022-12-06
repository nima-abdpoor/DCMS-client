package com.nima.dcms

import android.content.Context
import com.nima.common.abstraction.MyDaoService
import com.nima.common.database.entitty.Config
import com.nima.common.database.getDao
import com.nima.common.implementation.MyDaoServiceImpl
import com.nima.network.manager.NetworkManager

class DCMS(private val ctx: Context) {
    private val manager: NetworkManager = NetworkManager(ctx)
    private lateinit var dbService: MyDaoService

    fun init(uniqueId: String, vararg info: String) {
        // TODO: sending user important information beside special uniqueId to get more information
    }

    fun init(uniqueId: String) {
        val db = getDao(ctx)
        dbService = MyDaoServiceImpl(db)
        manager.submitWork()
    }

    private suspend fun readDataFromConfigDataBase(): Config {
        return dbService.getConfig()
    }
}