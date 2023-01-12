package com.nima.dcms

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import com.nima.dcms.interceptor.DCMSInterceptor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val gson = GsonBuilder()
            .setLenient()
            .create()
        DCMS(this).init("6660283978")
        val builder = Retrofit
            .Builder()
            .baseUrl("https://dummyjson.com/")
            .client(createOKHttpClientDefault(this))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        val retro = builder.create(API::class.java)
        retro.apply {
            findViewById<TextView>(R.id.button1).setOnClickListener { callRequest { getCart() } }
            findViewById<TextView>(R.id.button2).setOnClickListener { callRequest { getSpecificProduct() } }
            findViewById<TextView>(R.id.button3).setOnClickListener { callRequest { getProductWithCategory() } }
            findViewById<TextView>(R.id.button4).setOnClickListener { callRequest { getProductSearchPhone() } }
            findViewById<TextView>(R.id.button5).setOnClickListener { callRequest { searchProduct() } }
            findViewById<TextView>(R.id.button6).setOnClickListener { callRequest { getProducts() } }
        }
    }
}

private fun createOKHttpClientDefault(context: Context): OkHttpClient {
    val builder = OkHttpClient.Builder()
    builder.readTimeout(
        20_000,
        TimeUnit.MILLISECONDS
    )
    builder.writeTimeout(
        20_000,
        TimeUnit.MILLISECONDS
    )
    builder.addInterceptor(DCMSInterceptor(context))
    val client = builder.build()
    client.dispatcher().maxRequests = 5
    return client
}

private fun callRequest(function: suspend () -> Response<ResponseClass>) {
    CoroutineScope(Dispatchers.IO).launch {
        function.invoke()
    }
}