package com.nima.common.utils

import android.util.Base64
import android.util.Log
import java.lang.Exception
import java.nio.charset.StandardCharsets
import java.security.SecureRandom
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

object AESGCMEncryption {
    @Throws(Exception::class)
    private fun encrypt(plainText: String, key: SecretKey?): ByteArray {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val nonce = ByteArray(12)
        SecureRandom().nextBytes(nonce)
        val spec = GCMParameterSpec(128, nonce)
        cipher.init(Cipher.ENCRYPT_MODE, key, spec)
        val cipherText = cipher.doFinal(plainText.toByteArray(StandardCharsets.UTF_8))
        val concatenatedArray = ByteArray(nonce.size + cipherText.size)
        System.arraycopy(nonce, 0, concatenatedArray, 0, nonce.size)
        System.arraycopy(cipherText, 0, concatenatedArray, nonce.size, cipherText.size)
        return concatenatedArray
    }

    fun encryptText(plainText: String): String? {
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(256)
        val key = keyGenerator.generateKey()
        val ciphertext = encrypt(plainText, key)
        val encodedKey = Base64.encodeToString(key.encoded, Base64.DEFAULT)
        val encodedNonce = Base64.encodeToString(ciphertext, Base64.DEFAULT)
        Log.d("TAG", "encryptText:encodedKey--> $encodedKey")
        return encodedNonce
    }
}