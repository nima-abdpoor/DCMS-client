package com.nima.dcms

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
//import com.nima.dcms.database.ObjectBox
//import com.nima.dcms.database.datasource.implementation.CacheDDImpl
//import com.nima.dcms.database.entitty.URLIdsEntity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        ObjectBox.init(this)
//        val dataSource = CacheDDImpl(ObjectBox.store)
//        ObjectBox.store.boxFor(URLIdsEntity::class.java).put(
//            URLIdsEntity(0, mutableListOf("1234", "124434"))
//        )
//        val results = dataSource.getAllURLIds()
//        Log.d("TAG", "onCreate: $results")
    }
}