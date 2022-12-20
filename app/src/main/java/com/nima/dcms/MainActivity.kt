package com.nima.dcms

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import com.nima.dcms.interceptor.DCMSInterceptor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.txt).setOnClickListener {
//            uploadData()
        }
        val gson = GsonBuilder()
            .setLenient()
            .create()
        DCMS(this).init("6660283978")
        val builder = Retrofit
            .Builder()
            .baseUrl("http://192.168.1.109:8080/")
            .client(createOKHttpClientDefault(this))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        val retro = builder.create(API::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            val res = retro.getFavorites()
            Log.d("TAG", "onCreate: ${res.body()}")
        }
    }
}

private fun createOKHttpClientDefault(context: Context): OkHttpClient {
    val builder = OkHttpClient.Builder()
    builder.readTimeout(
        10_000,
        TimeUnit.MILLISECONDS
    )
    builder.writeTimeout(
        10_000,
        TimeUnit.MILLISECONDS
    )
    builder.addInterceptor(DCMSInterceptor(context))
    val client = builder.build()
    client.dispatcher().maxRequests = 5
    return client
}