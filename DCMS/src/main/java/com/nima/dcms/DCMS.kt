package com.nima.dcms

import android.content.Context
import com.nima.network.manager.NetworkManager

class DCMS(private val context: Context) {
    private val manager: NetworkManager = NetworkManager(context)

    fun init(uniqueId: String, vararg info: String) {
        // TODO: sending user important information beside special uniqueId to get more information
    }

    fun init(uniqueId: String) {
        manager.submitWork()
    }
}