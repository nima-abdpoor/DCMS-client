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
}