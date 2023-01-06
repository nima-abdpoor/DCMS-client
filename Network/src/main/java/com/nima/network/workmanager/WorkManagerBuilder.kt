package com.nima.network.workmanager

import androidx.work.*
import java.util.concurrent.TimeUnit

class WorkManagerBuilder {


    inline fun <reified T : ListenableWorker> getOneTimeWorkRequest(
        constraints: Constraints? = null,
        initialDelay: Pair<Long, TimeUnit>? = null
    ): OneTimeWorkRequest {
        val requestBuilder = OneTimeWorkRequestBuilder<T>()
        constraints?.let { requestBuilder.setConstraints(it) }
        initialDelay?.let { requestBuilder.setInitialDelay(it.first, it.second) }
        return requestBuilder.build()
    }

    inline fun <reified T : ListenableWorker> getPeriodicWorkRequest(
        repeatInterval: Long,
        repeatIntervalTimeUnit: TimeUnit,
        constraints: Constraints? = null,
        initialDelay: Pair<Long, TimeUnit>? = null
    ): PeriodicWorkRequest {
        val requestBuilder = PeriodicWorkRequestBuilder<T>(repeatInterval, repeatIntervalTimeUnit)
        constraints?.let { requestBuilder.setConstraints(it) }
        initialDelay?.let { requestBuilder.setInitialDelay(it.first, it.second) }
        return requestBuilder.build()
    }
}