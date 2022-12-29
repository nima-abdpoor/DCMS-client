package com.nima.dcms

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.GET

interface API {

    @GET("config")
    suspend fun getFavorites(): Response<ResponseClass>

    @GET("products")
    suspend fun getProducts(): Response<ResponseClass>

    @GET("products/1")
    suspend fun getSpecificProduct(): Response<ResponseClass>

    @GET("products/search?q=phone")
    suspend fun getProductSearchPhone(): Response<ResponseClass>

    @GET("products/category/smartphones")
    suspend fun getProductWithCategory(): Response<ResponseClass>

    @GET("products?limit=10&skip=10&select=title,price")
    suspend fun searchProduct(): Response<ResponseClass>

    //https://dummyjson.com/carts/1
    @GET("carts/1")
    suspend fun getCart(): Response<ResponseClass>

}

data class ResponseClass(
    @SerializedName("config")
    val number: String? = null,
)

data class Config(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("is_live")
    val is_live: String? = null,
    @SerializedName("sync_type")
    val sync_type: String? = null,
)