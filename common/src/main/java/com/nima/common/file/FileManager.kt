package com.nima.common.file

import android.content.Context
import com.nima.common.utils.DCMS_FILE_NAME

class FileManager(private val context: Context) {
    private val isFirstFileUsing = false
    private val isSecondFileUsing = false

    fun writeIntoFile(text: String) {
        context.openFileOutput(getFileName(), Context.MODE_APPEND).use { output ->
            output.write(text.toByteArray())
        }
    }

    private fun getFileName(): String {
        return if (!isFirstFileUsing)
            context.packageName + "-" + DCMS_FILE_NAME + "-FIRST.txt"
        else context.packageName + "-" + DCMS_FILE_NAME + "-SECOND.txt"
    }


}