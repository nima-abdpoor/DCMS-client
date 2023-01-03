package com.nima.network.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.nima.common.database.sharedpref.SharedPreferencesHelper
import com.nima.common.file.FileManager
import com.nima.common.utils.*
import com.nima.network.manager.request.FileUploaderHttpRequestBuilder

class UploadLogFileWorker(private val appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {
    private val pref = SharedPreferencesHelper()
    private val fileManager = FileManager(appContext)
    private val request = FileUploaderHttpRequestBuilder(BASE_URL + UPLOAD_LOG_FILE_URL)
    private var currentBaseUrlIndex = 0


    override fun doWork(): Result {
        return callUploadFileRout()
    }

    private fun callUploadFileRout(): Result {
        getFileWhichIsReadyToUpload()?.let { fileName ->
            val currentFile = appContext.packageName + "-" + DCMS_FILE_NAME + "-" + fileName
            return try {
                pref.saveFileStatus(fileName, true)
                request.addFilePart("log", appContext, currentFile)
                val response = request.finish()
                fileManager.writeIntoFile("")
                Result.success()
            } catch (e: Exception) {
                Log.d("TAG", "callUploadFileRout: ${e.message}")
                pref.saveFileStatus(fileName, false)
                Result.retry()
            }
        } ?: kotlin.run {
            return Result.success()
        }
    }

    private fun isFileHasContent(currentFile: String): Boolean {
        val fileName = appContext.packageName + "-" + DCMS_FILE_NAME + "-" + currentFile
        appContext.openFileInput(fileName).use { stream ->
            val text = stream.bufferedReader().use {
                it.readText()
            }
            return text.isNotEmpty()
        }
    }

    private fun getFilesWithContent(): MutableList<String> {
        val files = mutableListOf<String>()
        if (isFileHasContent(DCMS_FIRST_FILE_NAME)) files.add(DCMS_FIRST_FILE_NAME)
        else if (isFileHasContent(DCMS_SECOND_FILE_NAME)) files.add(DCMS_SECOND_FILE_NAME)
        return files
    }

    //getting the file that is not read/write by another process
    private fun isFileFree(fileName: String) = when (fileName) {
        DCMS_FIRST_FILE_NAME -> !pref.getFirstFileReadingStatus()
        DCMS_SECOND_FILE_NAME -> !pref.getSecondFileReadingStatus()
        else -> false
    }


    private fun getFileWhichIsReadyToUpload(): String? {
        getFilesWithContent().forEach {
            if (isFileFree(it)) return it
        }
        return null
    }
}