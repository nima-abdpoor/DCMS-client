package com.nima.dcms.urlconverter

interface URLConverter {
    fun convert(url: String): Long
}