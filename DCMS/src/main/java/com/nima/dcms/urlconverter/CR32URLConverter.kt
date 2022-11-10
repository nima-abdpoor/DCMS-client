package com.nima.dcms.urlconverter

import java.util.zip.CRC32

class CR32URLConverter : URLConverter {
    override fun convert(url: String): Long {
        val crc = CRC32()
        crc.update(url.toByteArray())
        return crc.value
    }
}