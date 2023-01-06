package com.nima.network.workmanager

import android.content.Context
import androidx.work.Constraints
import androidx.work.Operation
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.nima.common.database.sharedpref.SharedPreferencesHelper
import com.nima.network.worker.ConfigRouteWorker
import com.nima.network.worker.UploadLogFileWorker
import com.nima.network.workmanager.constraint.mapToTimeUnit
import java.util.concurrent.TimeUnit

class NetworkManager(
    private val context: Context, repeatInterval: Long,
    repeatIntervalTimeUnit: String,
    constraints: Constraints? = null,
    initialDelay: Pair<Long, TimeUnit>? = null
) {
    private val getConfigWorker: WorkRequest
    private val uploadLogFileWorker: WorkRequest
    private val workManagerBuilder = WorkManagerBuilder()
    private val pref = SharedPreferencesHelper()

    init {
        getConfigWorker =
            workManagerBuilder.getOneTimeWorkRequest<ConfigRouteWorker>(
                constraints = constraints,
                initialDelay = initialDelay
            )

        uploadLogFileWorker =
            workManagerBuilder.getPeriodicWorkRequest<UploadLogFileWorker>(
                repeatInterval,
                repeatIntervalTimeUnit.mapToTimeUnit(),
                constraints,
                initialDelay,
            )
    }

    fun submitWork() {
        var result: Operation
        if (!pref.getConfigWorkerStatus())
            result = WorkManager
                .getInstance(context)
                .enqueue(getConfigWorker)

        if (!pref.getUploadWorkerStatus())
            result = WorkManager.getInstance(context).enqueue(uploadLogFileWorker)
    }
}