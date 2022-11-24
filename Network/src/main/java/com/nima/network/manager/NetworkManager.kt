package com.nima.network.manager

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest

class NetworkManager(private val context: Context) {
    private val uploadWorkRequest: WorkRequest

    init {
        uploadWorkRequest =
            OneTimeWorkRequestBuilder<ConfigRouteManager>()
                .build()
    }

    fun submitWork() {
        val result = WorkManager
            .getInstance(context)
            .enqueue(uploadWorkRequest)
    }
}