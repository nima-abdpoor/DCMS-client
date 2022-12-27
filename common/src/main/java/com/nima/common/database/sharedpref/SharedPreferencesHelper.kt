package com.nima.common.database.sharedpref

import android.content.Context
import com.nima.common.utils.UNIQUE_ID_KEY

class SharedPreferencesHelper {
    private val pref = SharedPreferences

    fun initSecureSharedPref(context: Context){
        pref.initSecureSharedPref(context)
    }

    fun saveUniqueId(uniqueId: String){
        pref.getSharedPref()?.edit()?.putString(UNIQUE_ID_KEY, uniqueId)?.apply()
    }

    fun getUniqueId() = pref.getSharedPref()?.getString(UNIQUE_ID_KEY, "") ?: ""
}