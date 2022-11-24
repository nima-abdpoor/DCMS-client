package com.nima.network.manager

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.nima.common.abstraction.MyDaoService
import com.nima.common.database.getDao
import com.nima.common.implementation.MyDaoServiceImpl

class NetworkManager(private val context: Context) {
    private val uploadWorkRequest: WorkRequest
    private val dbService: MyDaoService

    init {
        val db = getDao(context)
        dbService = MyDaoServiceImpl(db)
        uploadWorkRequest =
            OneTimeWorkRequestBuilder<UploadDataManager>()
                .build()
    }

    fun submitWork() {
        val result = WorkManager
            .getInstance(context)
            .enqueue(uploadWorkRequest)
    }
}