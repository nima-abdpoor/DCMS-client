package com.nima.dcms

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.GET

interface API {

    @GET("config")
    suspend fun getFavorites(): Response<ResponseClass>

}

data class ResponseClass(
    @SerializedName("config")
    val number: Config? = null,
)

data class Config(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("is_live")
    val is_live: String? = null,
    @SerializedName("sync_type")
    val sync_type: String? = null,
)