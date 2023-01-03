package com.nima.common.utils

import android.content.Context

fun Context.getFullLoggedFileName(fileName: String): String {
    return "$packageName-$DCMS_FILE_NAME-$fileName"
}