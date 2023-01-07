package com.nima.dcms

import android.content.Context
import com.nima.common.abstraction.MyDaoService
import com.nima.common.database.entitty.Config
import com.nima.common.database.getDao
import com.nima.common.database.sharedpref.SharedPreferencesHelper
import com.nima.common.implementation.MyDaoServiceImpl
import com.nima.network.workmanager.NetworkManager
import com.nima.network.workmanager.constraint.getWorkMangerConstraints
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DCMS(private val ctx: Context) {
    private lateinit var dbService: MyDaoService
    private val pref = SharedPreferencesHelper()
    fun init(uniqueId: String, vararg info: String) {
        pref.initSecureSharedPref(ctx)
        saveUniqueId(uniqueId)
        val db = getDao(ctx)
        dbService = MyDaoServiceImpl(db)
        CoroutineScope(Dispatchers.IO).launch {
            val deferredConfig = async { readDataFromConfigDataBase() }
            val config = deferredConfig.await()
            val manager = NetworkManager(
                ctx,
                config.repeatInterval,
                config.repeatIntervalTimeUnit,
                config.getWorkMangerConstraints(),
                initialDelay = null
            )
            manager.submitWork()
        }

        // TODO: sending users information beside special uniqueId to get more information
    }

    private fun saveUniqueId(uniqueId: String) {
        pref.saveUniqueId(uniqueId)
    }

//    fun init() {
//        SharedPreferences.initSecureSharedPref(ctx)
//        val db = getDao(ctx)
//        dbService = MyDaoServiceImpl(db)
//        manager.submitWork()
//    }

    private suspend fun readDataFromConfigDataBase(): Config {
        return dbService.getConfig()
    }
}