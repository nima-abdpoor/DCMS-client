package com.nima.network.workmanager

import android.content.Context
import android.util.Log
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.nima.common.database.sharedpref.SharedPreferencesHelper
import com.nima.common.utils.BASE_URL
import com.nima.common.utils.UPLOAD_LOG_FILE_URL
import com.nima.network.worker.ConfigRouteWorker
import com.nima.network.worker.UploadLogFileWorker
import com.nima.network.workmanager.constraint.WorkManagerConstraints
import com.nima.network.workmanager.constraint.getWorkMangerConstraints
import com.nima.network.workmanager.constraint.mapToTimeUnit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NetworkManager(
    private val context: Context, private val repeatInterval: Long,
    private val repeatIntervalTimeUnit: String,
    private val constraints: WorkManagerConstraints? = null,
    private val initialDelay: Pair<Long, String>? = null
) {
    private val configRouteWorker = ConfigRouteWorker(context)

    private val workManagerBuilder = WorkManagerBuilder()
    private val pref = SharedPreferencesHelper()

    fun submitWork() {
        CoroutineScope(Dispatchers.IO).launch {
            configRouteWorker.doWork().onSuccess {
                Log.d("TAG", "submitWork: $it")
                Log.d("TAG", "submitWork: asdf --> ${pref.getUploadWorkerStatus()}")
                if (!pref.getUploadWorkerStatus()) {
                    Log.d("TAG", "submitWork: started!!")
                    val uploadLogFileWorker =
                        workManagerBuilder.getPeriodicWorkRequest<UploadLogFileWorker>(
                            it.repeatInterval,
                            it.repeatIntervalTimeUnit.mapToTimeUnit(),
                            it.getWorkMangerConstraints()?.getConstraint(),
                            initialDelay,
                            workDataOf("UPLOAD_URL" to BASE_URL + UPLOAD_LOG_FILE_URL + pref.getUniqueId())
                        )
                    val result = WorkManager.getInstance(context)
                        .enqueue(uploadLogFileWorker)
                }
            }
        }
    }
}