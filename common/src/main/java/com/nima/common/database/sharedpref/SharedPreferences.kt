package com.nima.common.database.sharedpref

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.nima.common.utils.SECURE_SHARED_PREFERENCE_FILE_NAME

object SharedPreferences {
    var sharedPreferences : SharedPreferences? = null

    fun initSecureSharedPref(context: Context) {
        val masterKey = MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        //create preferences
        sharedPreferences = EncryptedSharedPreferences.create(
            context,
            SECURE_SHARED_PREFERENCE_FILE_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)
    }

    fun getSharedPref(): SharedPreferences? = sharedPreferences
}