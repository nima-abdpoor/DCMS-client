package com.nima.network.worker

import android.content.Context
import android.util.Log
import com.nima.common.abstraction.MyDaoService
import com.nima.common.database.entitty.*
import com.nima.common.database.getDao
import com.nima.common.database.sharedpref.SharedPreferencesHelper
import com.nima.common.implementation.MyDaoServiceImpl
import com.nima.common.model.ConfigBody
import com.nima.common.utils.BASE_URL
import com.nima.common.utils.CONFIG_URL
import com.nima.network.manager.model.HttpMethods
import com.nima.network.manager.request.HttpRequestBuilder
import com.nima.network.manager.wrapper.ResultWrapper
import kotlinx.coroutines.runBlocking

class ConfigRouteWorker(appContext: Context) {
    private val pref = SharedPreferencesHelper()
    private val dbService: MyDaoService
    private var currentBaseUrlIndex = 0

    init {
        val db = getDao(appContext)
        dbService = MyDaoServiceImpl(db)
    }

    suspend fun doWork(): Result<ConfigBody> {
        pref.saveGetConfigWorkerStatus(SharedPreferencesHelper.WorkerStatus.Started)
        return callConfigRoute()
    }
//    192.168.43.145, 192.168.1.111

    private suspend fun callConfigRoute(): Result<ConfigBody> {
        val request = HttpRequestBuilder()
            .setUrl(CONFIG_URL + pref.getUniqueId())
            .setMethod(HttpMethods.GET)
            .submit<ConfigBody>(ConfigBody())
        when (request) {
            is ResultWrapper.GenericError -> {
                Log.d("TAG", "uploadDataTest:GenericError ${request.error}")
                return handleUnsuccessfulResponse()
            }
            is ResultWrapper.NetworkError -> {
                Log.d("TAG", "uploadDataTest:NetworkError ${request.error}")
                return handleUnsuccessfulResponse()
            }
            is ResultWrapper.Success -> {
                val config = request.value?.body
                storeConfigDataToDataBase(config)
                Log.d("TAG", "uploadDataTest: $config")
                config?.let {
                     return Result.success(config)
                } ?: kotlin.run {
                    return Result.failure<ConfigBody>(Exception())
                }
            }
            else -> {
                return Result.failure<ConfigBody>(Exception())
//                return@runBlocking handleUnsuccessfullResponse()
            }
        }
    }

    private fun handleUnsuccessfulResponse(): Result<ConfigBody> {
        return Result.failure(Exception())
//        val urls = readDataFromConfigDataBase()
//        return if (urls.size >= currentBaseUrlIndex
//        ) {
//            changeBaseUrl(urls[currentBaseUrlIndex])
//            currentBaseUrlIndex++
//        } else Result.failure()
    }

    private suspend fun changeBaseUrl(requestUrl: RequestUrl) {
        val config = readDataFromConfigDataBase()
        BASE_URL = requestUrl.url ?: BASE_URL
    }

    private suspend fun storeConfigDataToDataBase(config: ConfigBody?) {
        dbService.insertConfig(config.toConfig())
        config?.urlIdFirst?.forEachIndexed { index, it ->
            dbService.insertURLFirst(URLIdFirst(id = index.toLong(), urlHash = it.id))
        }
        var regexId = 0
        config?.urlIdSecond?.forEachIndexed { index, it ->
            dbService.insertURLSecond(
                URLIdSecond(
                    id = index.toLong(),
                    urlHash = it.id
                )
            )
            it.regex.forEachIndexed { _, regex ->
                dbService.insertRegex(
                    Regex(
                        id = regexId.toLong(),
                        urlId = index.toLong(),
                        regex = regex.regex,
                        startIndex = regex.startIndex,
                        finishIndex = regex.finishIndex,
                    )
                )
                regexId++
            }
        }
        config?.validRequestUrls?.forEachIndexed { index, it ->
            dbService.insertRequestUrl(
                RequestUrl(
                    id = index.toLong(),
                    url = it
                )
            )
        }
    }

    private suspend fun readDataFromConfigDataBase(): List<RequestUrl> {
        return dbService.getAllRequestUrl()
    }
}