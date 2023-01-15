package com.nima.common.database.sharedpref

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.nima.common.utils.ENCRYPTION_KEY
import com.nima.common.utils.SECURE_SHARED_PREFERENCE_FILE_NAME
import javax.crypto.KeyGenerator

object SharedPreferences {
    private var sharedPreferences: SharedPreferences? = null

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
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        createEncryptionKey()
    }

    private fun createEncryptionKey() {
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(256)
        val key = keyGenerator.generateKey()
        val encodedKey = Base64.encodeToString(key.encoded, Base64.DEFAULT)
        sharedPreferences?.edit()?.putString(ENCRYPTION_KEY, encodedKey)?.apply()
    }

    fun getSharedPref(): SharedPreferences? = sharedPreferences
}