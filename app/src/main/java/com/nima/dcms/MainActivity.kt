package com.nima.dcms

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.nima.dcms.datasource.database.getDao
import com.nima.dcms.datasource.implementation.MyDaoServiceImpl
import com.nima.network.manager.NetworkManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.io.BufferedOutputStream
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val db = getDao(this)
        val service = MyDaoServiceImpl(db)
        findViewById<TextView>(R.id.txt).setOnClickListener {
//            uploadData()
        }
//        CoroutineScope(Dispatchers.IO).launch {
////            val ids = service.insertURL(URLIdFirst(null, 1254444))
////            val ids2 = service.insertURL(URLIdFirst(null, 15455))
////            val ids4 = service.insertURL(URLIdFirst(null, 2544))
//        }
//        CoroutineScope(Dispatchers.IO).launch {
//            val ids = service.getAllUrlFirst()
//            ids.forEach{
//                Log.d("TAG", "onCreate: ${it.urlId}")
//            }
//        }
        val manager = NetworkManager()
        manager.submitWork(this)
    }
}