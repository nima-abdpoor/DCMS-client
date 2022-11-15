package com.nima.network.manager

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class UploadDataManager(appContext: Context, workerParams: WorkerParameters):
    Worker(appContext, workerParams) {
    override fun doWork(): Result {
        uploadData()
        return Result.success()
    }

    private fun uploadData() {

    }
}