package com.nima.common.mappers

import com.nima.common.model.ConfigBody
import com.nima.common.model.ResponseClass
import org

fun ResponseClass.mapToProperModel(result: String): ResponseClass {
    return when(this){
        is ConfigBody -> result.toConfigBodyMapper()
    }
}


fun String.toConfigBodyMapper(): ConfigBody {
    val objError = JSONObject(errorBody)
    return ConfigBody(

    )
}