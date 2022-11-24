package com.nima.dcms

import android.content.Context
import com.nima.common.abstraction.MyDaoService
import com.nima.common.database.getDao
import com.nima.common.implementation.MyDaoServiceImpl
import com.nima.network.manager.NetworkManager

class DCMS(private val context: Context) {
    private val manager: NetworkManager
    private val dbService: MyDaoService

    init {
        val db = getDao(context)
        dbService = MyDaoServiceImpl(db)
        manager = NetworkManager()
    }

    fun init(uniqueId: String, vararg info: String) {
        // TODO: sending user important information beside special uniqueId to get more information
    }

    fun init(uniqueId: String) {
        manager.submitWork(context)
    }
}