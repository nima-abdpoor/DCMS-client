package com.nima.dcms.base

import android.content.Context

open class BaseDCMS {
    private lateinit var context : Context

    fun setContext(context: Context){
        this.context = context
    }

    fun getContext(): Context {
        return this.context
    }
}