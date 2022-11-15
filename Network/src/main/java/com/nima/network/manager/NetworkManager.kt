package com.nima.network.manager

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest

class NetworkManager() {
    private val uploadWorkRequest: WorkRequest

    init {
        uploadWorkRequest =
            OneTimeWorkRequestBuilder<UploadDataManager>()
                .build()
    }

    fun submitWork(context: Context) {
        WorkManager
            .getInstance(context)
            .enqueue(uploadWorkRequest)
    }
}