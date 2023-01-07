package com.nima.network.workmanager.constraint

import java.util.concurrent.TimeUnit

fun String.mapToTimeUnit(): TimeUnit {
    return when(this){
        "0" -> TimeUnit.NANOSECONDS
        "1" -> TimeUnit.MICROSECONDS
        "2" -> TimeUnit.MICROSECONDS
        "3" -> TimeUnit.SECONDS
        "4" -> TimeUnit.MINUTES
        "5" -> TimeUnit.HOURS
        else -> TimeUnit.DAYS
    }
}