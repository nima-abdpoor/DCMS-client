package com.nima.network.manager

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.nima.network.worker.ConfigRouteWorker
import com.nima.network.worker.UploadLogFileWorker

class NetworkManager(private val context: Context) {
    private val getConfigWorker: WorkRequest
    private val uploadLogFileWorker: WorkRequest

    init {
        getConfigWorker =
            OneTimeWorkRequestBuilder<ConfigRouteWorker>()
                .build()
        uploadLogFileWorker =
            OneTimeWorkRequestBuilder<UploadLogFileWorker>()
                .build()
    }

    fun submitWork() {
        var result = WorkManager
            .getInstance(context)
            .enqueue(getConfigWorker)

        result = WorkManager.getInstance(context).enqueue(uploadLogFileWorker)
    }
}