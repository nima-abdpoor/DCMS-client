package com.nima.dcms.urlconverter

import android.R.string
import java.nio.charset.StandardCharsets
import java.security.MessageDigest


class MD5URLConverter : URLConverter {
    override fun convert(url: String): Int {
        val md5: MessageDigest = MessageDigest.getInstance("MD5")
        md5.update(StandardCharsets.UTF_8.encode(url))
//        return java.lang.String.format("%032x", BigInteger(1, md5.digest()))
        return 1
    }
}