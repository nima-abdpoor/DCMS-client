package com.nima.network.manager

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.nima.common.abstraction.MyDaoService
import com.nima.common.database.entitty.Config
import com.nima.common.database.entitty.URLIdFirst
import com.nima.common.database.entitty.URLIdSecond
import com.nima.common.database.entitty.toConfig
import com.nima.common.database.getDao
import com.nima.common.implementation.MyDaoServiceImpl
import com.nima.common.model.ConfigBody
import com.nima.common.utils.BASE_URL
import com.nima.network.manager.model.HttpMethods
import com.nima.network.manager.request.HttpRequestBuilder
import com.nima.network.manager.wrapper.ResultWrapper
import kotlinx.coroutines.runBlocking

class ConfigRouteManager(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {
    private val dbService: MyDaoService
    private var baseUrls: List<String> = arrayListOf(BASE_URL)
    private var currentBaseUrl = 0

    init {
        val db = getDao(appContext)
        dbService = MyDaoServiceImpl(db)
    }

    override fun doWork(): Result {
        return callConfigRoute()
    }
//    192.168.43.145, 192.168.1.111

    private fun callConfigRoute() = runBlocking {
        val request = HttpRequestBuilder()
            .setUrl("$BASE_URL/config")
            .setMethod(HttpMethods.POST)
            .submit<ConfigBody>(ConfigBody())
        when (request) {
            is ResultWrapper.GenericError -> {
                Log.d("TAG", "uploadDataTest:GenericError ${request.error}")
                return@runBlocking handleUnsuccessfullResponse()
            }
            is ResultWrapper.NetworkError -> {
                Log.d("TAG", "uploadDataTest:NetworkError ${request.error}")
                return@runBlocking handleUnsuccessfullResponse()
            }
            is ResultWrapper.Success -> {
                val config = request.value?.body
                storeConfigDataToDataBase(config)
                Log.d("TAG", "uploadDataTest: $config")
                return@runBlocking Result.success()
            }
            else -> {
                return@runBlocking handleUnsuccessfullResponse()
            }
        }
    }

    private suspend fun handleUnsuccessfullResponse(): Result {
        return if ((readDataFromConfigDataBase().validRequestUrls?.split(",")?.size
                ?: 1) >= currentBaseUrl
        ) {
            currentBaseUrl++
            changeBaseUrl()
            Result.retry()
        } else Result.failure()
    }

    private suspend fun changeBaseUrl() {
        val config = readDataFromConfigDataBase()
        baseUrls = config.validRequestUrls?.split(",") ?: arrayListOf(BASE_URL)
        BASE_URL = baseUrls[currentBaseUrl]
    }

    private suspend fun storeConfigDataToDataBase(config: ConfigBody?) {
        dbService.insertConfig(config.toConfig())
        config?.urlIdFirst?.forEachIndexed { index, it ->
            dbService.insertURLFirst(URLIdFirst(id = index.toLong(), urlId = it.id))
        }
        config?.urlIdSecond?.forEachIndexed { index, it ->
            dbService.insertURLSecond(
                URLIdSecond(
                    id = index.toLong(),
                    urlId = it.id,
                    regex = it.regex,
                    startIndex = it.startIndex,
                    finishIndex = it.finishIndex
                )
            )
        }
    }

    private suspend fun readDataFromConfigDataBase(): Config {
        return dbService.getConfig()
    }
}