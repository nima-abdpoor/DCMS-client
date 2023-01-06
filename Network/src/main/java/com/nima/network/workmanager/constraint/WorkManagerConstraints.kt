package com.nima.network.workmanager.constraint

import android.os.Build
import androidx.work.Constraints
import androidx.work.NetworkType

class WorkManagerConstraints {
    private val constraints = Constraints.Builder()
        .setRequiresBatteryNotLow(true)
        .setRequiresStorageNotLow(true)

    fun setNetworkType(type: String) {
        constraints.setRequiredNetworkType(getNetworkTypeByString(type))
    }

    fun setBatteryNotLow(requiresBatteryNotLow: Boolean){
        constraints.setRequiresBatteryNotLow(requiresBatteryNotLow)
    }

    fun setRequiresCharging(requiresCharging: Boolean){
        constraints.setRequiresCharging(requiresCharging)
    }

    fun setDeviceIdl(requiresDeviceIdle: Boolean){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            constraints.setRequiresDeviceIdle(requiresDeviceIdle)
        }
    }

    fun setStorageNotLow(requiresStorageNotLow: Boolean){
        constraints.setRequiresStorageNotLow(requiresStorageNotLow)
    }

    fun getConstraint() = constraints

    private fun getNetworkTypeByString(type: String): NetworkType {
        return when (type) {
            "0" -> NetworkType.NOT_REQUIRED
            "2" -> NetworkType.UNMETERED
            "3" -> NetworkType.NOT_ROAMING
            "4" -> NetworkType.METERED
            "5" -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                NetworkType.TEMPORARILY_UNMETERED
            } else {
                NetworkType.CONNECTED
            }
            else -> NetworkType.CONNECTED
        }
    }
}