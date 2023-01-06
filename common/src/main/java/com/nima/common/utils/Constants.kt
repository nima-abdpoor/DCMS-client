package com.nima.common.utils

import android.icu.util.TimeUnit

var BASE_URL = "http://192.168.1.111:8080"
var UNIQUE_ID = ""
var UNIQUE_ID_KEY = "DCMS_UNIQUE_ID_KEY"
var CONFIG_URL = "$BASE_URL/config/"
const val UPLOAD_LOG_FILE_URL = "/sendLogFile/"
const val SEND_LOG_URL = "/sendLog/"

var SECURE_SHARED_PREFERENCE_FILE_NAME = "DCMS_SECURE_PREF"

const val DCMS_FILE_NAME = "DCMS-FILE"
const val DCMS_FIRST_FILE_NAME = "FIRST.txt"
const val DCMS_SECOND_FILE_NAME = "SECOND.txt"

const val FIRST_FILE_READING_STATUS_KEY = "FIRST_FILE_READING_STATUS_KEY"
const val SECOND_FILE_READING_STATUS_KEY = "SECOND_FILE_READING_STATUS_KEY"

//Upload Worker
const val UPLOAD_WORKER_STARTED_STATUS_KEY = "UPLOAD_WORKER_STARTED_STATUS_KEY"

//DEFAULT CONSTRAINTS
const val DEFAULT_REPEAT_INTERVAL_WORKER_TIME = 1L
val DEFAULT_REPEAT_INTERVAL_WORK_TIME_UNIT = java.util.concurrent.TimeUnit.DAYS