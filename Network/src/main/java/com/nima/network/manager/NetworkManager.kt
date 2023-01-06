package com.nima.network.manager

import android.content.Context
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.nima.common.database.sharedpref.SharedPreferencesHelper
import com.nima.network.worker.ConfigRouteWorker
import com.nima.network.worker.UploadLogFileWorker
import java.util.concurrent.TimeUnit

class NetworkManager(
    private val context: Context, repeatInterval: Long,
    repeatIntervalTimeUnit: TimeUnit,
) {
    private val getConfigWorker: WorkRequest
    private val uploadLogFileWorker: WorkRequest
    private val workManagerBuilder = WorkManagerBuilder()
    private val pref = SharedPreferencesHelper()

    init {
        getConfigWorker = workManagerBuilder.getOneTimeWorkRequest<ConfigRouteWorker>()

        uploadLogFileWorker =
            workManagerBuilder.getPeriodicWorkRequest<UploadLogFileWorker>(
                repeatInterval,
                repeatIntervalTimeUnit,
                null,
                null,
            )
    }

    fun submitWork() {
        var result = WorkManager
            .getInstance(context)
            .enqueue(getConfigWorker)

        if (!pref.getUploadWorkerStatus())
            result = WorkManager.getInstance(context).enqueue(uploadLogFileWorker)
    }
}