package com.nima.network.workmanager

import android.content.Context
import android.util.Log
import androidx.work.Operation
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.nima.common.database.sharedpref.SharedPreferencesHelper
import com.nima.network.worker.ConfigRouteWorker
import com.nima.network.worker.UploadLogFileWorker
import com.nima.network.workmanager.constraint.WorkManagerConstraints
import com.nima.network.workmanager.constraint.mapToTimeUnit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NetworkManager(
    private val context: Context, private val repeatInterval: Long,
    private val repeatIntervalTimeUnit: String,
    private val constraints: WorkManagerConstraints? = null,
    private val initialDelay: Pair<Long, String>? = null
) {
    private val getConfigWorker: WorkRequest

    //    private val uploadLogFileWorker: WorkRequest
    private val workManagerBuilder = WorkManagerBuilder()
    private val pref = SharedPreferencesHelper()

    init {
        getConfigWorker =
            workManagerBuilder.getOneTimeWorkRequest<ConfigRouteWorker>(
                constraints = constraints?.getConstraint(),
                initialDelay = initialDelay
            )
    }

    suspend fun submitWork() {
        if (!pref.getConfigWorkerStatus()) {
            var result = WorkManager
                .getInstance(context)
                .enqueue(getConfigWorker)
            result.state.observeForever {
                CoroutineScope(Dispatchers.Main).launch {
                    it?.let {
                        Log.d("TAG", "submitWork: $it")
                        if (it.toString() == "SUCCESS") {
                            Log.d("TAG", "submitWork: success")
                            withContext(Dispatchers.IO) {
                                if (!pref.getUploadWorkerStatus()) {
                                    val uploadLogFileWorker =
                                        workManagerBuilder.getPeriodicWorkRequest<UploadLogFileWorker>(
                                            repeatInterval,
                                            repeatIntervalTimeUnit.mapToTimeUnit(),
                                            constraints?.getConstraint(),
                                            initialDelay,
                                        )
                                    result = WorkManager.getInstance(context)
                                        .enqueue(uploadLogFileWorker)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}