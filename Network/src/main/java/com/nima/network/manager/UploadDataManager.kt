package com.nima.network.manager

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.nima.network.manager.model.HttpMethods
import com.nima.network.manager.request.HttpRequestBuilder
import kotlinx.coroutines.runBlocking
import java.io.BufferedOutputStream
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class UploadDataManager(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {
    override fun doWork(): Result {
        uploadDataTwo()
        return Result.success()
    }

    private fun uploadDataTwo(){
        runBlocking {
            val request = HttpRequestBuilder()
                .setUrl("http://google.com")
                .setMethod(HttpMethods.GET)
                .submit()
            Log.d("TAG", "uploadDataTwo: " +
                    "code:${request.responseCode}" +
                    "message:${request.responseMessage}")
        }


    }

    private fun uploadData() {
        runBlocking {
            val myUrl = "http://google.com"
            val data = "data"
            try {
                val url = URL(myUrl)
                val httpURLConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
                // setting the  Request Method Type
                httpURLConnection.requestMethod = "GET"
                // adding the headers for request
                httpURLConnection.setRequestProperty("Content-Type", "application/json")
                try {
                    //to tell the connection object that we will be wrting some data on the server and then will fetch the output result
                    httpURLConnection.doOutput = true
                    // this is used for just in case we don't know about the data size associated with our request
                    httpURLConnection.setChunkedStreamingMode(0)

                    // to write tha data in our request
                    val outputStream: OutputStream =
                        BufferedOutputStream(httpURLConnection.outputStream)
                    val outputStreamWriter = OutputStreamWriter(outputStream)
                    outputStreamWriter.write(data)
                    outputStreamWriter.flush()
                    outputStreamWriter.close()

                    // to log the response code of your request
                    Log.d(
                        "ApplicationConstant.TAG",
                        "MyHttpRequestTask doInBackground : " + httpURLConnection.responseCode
                    )
                    // to log the response message from your server after you have tried the request.
                    Log.d(
                        "ApplicationConstant.TAG",
                        "MyHttpRequestTask doInBackground : " + httpURLConnection.responseMessage
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    // this is done so that there are no open connections left when this task is going to complete
                    httpURLConnection.disconnect()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}