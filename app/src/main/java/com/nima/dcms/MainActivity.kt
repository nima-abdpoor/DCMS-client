package com.nima.dcms

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.nima.dcms.datasource.database.getDao
import com.nima.dcms.datasource.implementation.MyDaoServiceImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val db = getDao(this)
        val service = MyDaoServiceImpl(db)
        CoroutineScope(Dispatchers.IO).launch {
//            val ids = service.insertURL(URLIdFirst(null, 1254444))
//            val ids2 = service.insertURL(URLIdFirst(null, 15455))
//            val ids4 = service.insertURL(URLIdFirst(null, 2544))
        }
        CoroutineScope(Dispatchers.IO).launch {
            val ids = service.getAllUrlFirst()
            ids.forEach{
                Log.d("TAG", "onCreate: ${it.urlId}")
            }
        }

    }
}