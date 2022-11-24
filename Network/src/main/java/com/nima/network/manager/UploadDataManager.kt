package com.nima.network.manager

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.nima.common.abstraction.MyDaoService
import com.nima.common.database.entitty.toConfig
import com.nima.common.database.getDao
import com.nima.common.implementation.MyDaoServiceImpl
import com.nima.common.model.ConfigBody
import com.nima.network.manager.model.HttpMethods
import com.nima.network.manager.request.HttpRequestBuilder
import com.nima.network.manager.wrapper.ResultWrapper
import kotlinx.coroutines.runBlocking

class UploadDataManager(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {
    private val dbService: MyDaoService

    init {
        val db = getDao(appContext)
        dbService = MyDaoServiceImpl(db)
    }

    override fun doWork(): Result {
        return uploadDataTest()
    }
//    192.168.43.145, 192.168.1.111

    private fun uploadDataTest() = runBlocking {
        val request = HttpRequestBuilder()
            .setUrl("http://192.168.1.111:8080/config")
            .setMethod(HttpMethods.POST)
            .submit<ConfigBody>(ConfigBody())
        when (request) {
            is ResultWrapper.GenericError -> {
                Log.d("TAG", "uploadDataTest:GenericError ${request.error}")
                return@runBlocking Result.failure()
            }
            is ResultWrapper.NetworkError -> {
                Log.d("TAG", "uploadDataTest:NetworkError ${request.error}")
                return@runBlocking Result.failure()
            }
            is ResultWrapper.Success -> {
                val config = request.value?.body
                Log.d("TAG", "uploadDataTest: $config")
                config?.validRequestUrls?.forEach {

                }
                dbService.insertConfig(config.toConfig())
                return@runBlocking Result.success()
            }
            else -> return@runBlocking Result.retry()
        }
    }
}