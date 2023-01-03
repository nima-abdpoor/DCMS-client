package com.nima.common.file

import android.content.Context
import com.nima.common.database.sharedpref.SharedPreferencesHelper
import com.nima.common.utils.DCMS_FIRST_FILE_NAME
import com.nima.common.utils.DCMS_SECOND_FILE_NAME
import com.nima.common.utils.getFullLoggedFileName

class FileManager(private val context: Context) {
    private val pref = SharedPreferencesHelper()

    fun writeIntoFile(text: String) {
        val fileName = getProperFileName()
        fileName?.let { file ->
            pref.saveFileStatus(file, true)
            context.openFileOutput(file, Context.MODE_APPEND).use { output ->
                output.write(text.toByteArray())
            }
            pref.saveFileStatus(fileName, false)
        }
    }

    fun writeIntoFile(text: String, fileName: String) {
        pref.saveFileStatus(fileName, true)
        context.openFileOutput(context.getFullLoggedFileName(fileName), Context.MODE_APPEND)
            .use { output ->
                output.write(text.toByteArray())
            }
        pref.saveFileStatus(fileName, false)
    }

    //getting the file that is not read/write by another process
    private fun isFileFree(fileName: String) = when (fileName) {
        DCMS_FIRST_FILE_NAME -> !pref.getFirstFileReadingStatus()
        DCMS_SECOND_FILE_NAME -> !pref.getSecondFileReadingStatus()
        else -> false
    }

    private fun getProperFileName(): String? {
        return if (isFileFree(DCMS_FIRST_FILE_NAME)) DCMS_FIRST_FILE_NAME
        else if (isFileFree(DCMS_SECOND_FILE_NAME)) DCMS_SECOND_FILE_NAME
        else null
    }
}