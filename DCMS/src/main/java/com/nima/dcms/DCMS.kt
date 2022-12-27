package com.nima.dcms

import android.content.Context
import com.nima.common.abstraction.MyDaoService
import com.nima.common.database.sharedpref.SharedPreferences
import com.nima.common.database.entitty.Config
import com.nima.common.database.getDao
import com.nima.common.database.sharedpref.SharedPreferencesHelper
import com.nima.common.implementation.MyDaoServiceImpl
import com.nima.common.utils.UNIQUE_ID_KEY
import com.nima.network.manager.NetworkManager

class DCMS(private val ctx: Context) {
    private val manager: NetworkManager = NetworkManager(ctx)
    private lateinit var dbService: MyDaoService
    private val pref = SharedPreferencesHelper()
    fun init(uniqueId: String, vararg info: String) {
        pref.initSecureSharedPref(ctx)
        saveUniqueId(uniqueId)
        val db = getDao(ctx)
        dbService = MyDaoServiceImpl(db)
        manager.submitWork()
        // TODO: sending users information beside special uniqueId to get more information
    }

    private fun saveUniqueId(uniqueId: String) {
        pref.saveUniqueId(uniqueId)
    }

    fun init() {
        SharedPreferences.initSecureSharedPref(ctx)
        val db = getDao(ctx)
        dbService = MyDaoServiceImpl(db)
        manager.submitWork()
    }

    private suspend fun readDataFromConfigDataBase(): Config {
        return dbService.getConfig()
    }
}