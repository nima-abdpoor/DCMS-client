package com.nima.network.manager

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.nima.common.model.ConfigBody
import com.nima.network.manager.model.HttpMethods
import com.nima.network.manager.request.HttpRequestBuilder
import com.nima.network.manager.wrapper.ResultWrapper
import kotlinx.coroutines.runBlocking

class UploadDataManager(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {
    override fun doWork(): Result {
        uploadDataTest()
        return Result.success()
    }

    private fun uploadDataTest() {
        runBlocking {
            val request = HttpRequestBuilder()
                .setUrl("http://192.168.43.145:8080/config")
                .setMethod(HttpMethods.POST)
                .submit<ConfigBody>(ConfigBody())
            when (request) {
                is ResultWrapper.GenericError -> {
                    Log.d("TAG", "uploadDataTwo:GenericError ${request.error?.message}")
                }
                is ResultWrapper.NetworkError -> {
                    Log.d("TAG", "uploadDataTwo:NetworkError ${request.error?.message}")
                }
                is ResultWrapper.Success -> {
                    Log.d("TAG", "uploadDataTwo:Success ${request.value?.body}")
                }
            }
        }
    }
}