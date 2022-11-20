package com.nima.network.manager.wrapper

import com.nima.common.model.ResponseClass

class Response<T> {
    var isSuccessful = false
    var body: ResponseClass? = null
}