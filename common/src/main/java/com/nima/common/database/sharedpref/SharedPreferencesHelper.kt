package com.nima.common.database.sharedpref

import android.content.Context
import com.nima.common.utils.*

class SharedPreferencesHelper {
    private val pref = SharedPreferences

    fun initSecureSharedPref(context: Context) {
        pref.initSecureSharedPref(context)
    }

    fun saveUniqueId(uniqueId: String) {
        pref.getSharedPref()?.edit()?.putString(UNIQUE_ID_KEY, uniqueId)?.apply()
    }

    fun getUniqueId() = pref.getSharedPref()?.getString(UNIQUE_ID_KEY, "") ?: ""

    private fun saveFirstFileReadingStatus(isOpen: Boolean) {
        pref.getSharedPref()?.edit()?.putBoolean(FIRST_FILE_READING_STATUS_KEY, isOpen)?.apply()
    }

    fun getFirstFileReadingStatus() =
        pref.getSharedPref()?.getBoolean(FIRST_FILE_READING_STATUS_KEY, false) ?: false

    private fun saveSecondFileReadingStatus(isOpen: Boolean) {
        pref.getSharedPref()?.edit()?.putBoolean(SECOND_FILE_READING_STATUS_KEY, isOpen)?.apply()
    }

    fun saveFileStatus(fileName: String, status: Boolean) {
        when (fileName) {
            DCMS_FIRST_FILE_NAME -> saveFirstFileReadingStatus(status)
            DCMS_SECOND_FILE_NAME -> saveSecondFileReadingStatus(status)
        }
    }

    fun getSecondFileReadingStatus() =
        pref.getSharedPref()?.getBoolean(SECOND_FILE_READING_STATUS_KEY, false) ?: false

    fun saveUploadLogWorkerStatus(worker: WorkerStatus) {
        pref.getSharedPref()?.edit()?.putBoolean(UPLOAD_WORKER_STARTED_STATUS_KEY, worker.status)?.apply()
    }

    fun getUploadWorkerStatus() =
        pref.getSharedPref()?.getBoolean(UPLOAD_WORKER_STARTED_STATUS_KEY, false) ?: false

    fun saveGetConfigWorkerStatus(worker: WorkerStatus) {
        pref.getSharedPref()?.edit()?.putBoolean(CONFIG_WORKER_STARTED_STATUS_KEY, worker.status)?.apply()
    }

    fun getConfigWorkerStatus() =
        pref.getSharedPref()?.getBoolean(CONFIG_WORKER_STARTED_STATUS_KEY, false) ?: false

    enum class WorkerStatus(val status: Boolean){
        Started(true), Init(false)
    }
}