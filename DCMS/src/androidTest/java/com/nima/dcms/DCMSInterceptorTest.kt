package com.nima.dcms

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.nima.dcms.interceptor.DCMSInterceptor
import com.nima.dcms.urlconverter.CR32URLConverter
import com.nima.dcms.urlconverter.URLConverter
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DCMSInterceptorTest {
    private lateinit var interceptor: DCMSInterceptor
    private lateinit var converter: URLConverter
    private val charPool: List<Char> = ('a'..'z') + ('A'..'Z')
    private val numberOfUrlHashes = 10

    @Before
    fun initValues() {
        converter = CR32URLConverter()
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        interceptor = DCMSInterceptor(appContext)
        generateUrlHash(numberOfUrlHashes)
        Assert.assertEquals(true, true)
    }

    private fun generateUrlHash(number: Int) {
        val res = generateString(number, 100)
        res.forEach {
            Log.d("TAG", "generateUrlHash: $it")
        }
    }

    private fun generateString(number: Int, length: Int): List<String> {
        val results = ArrayList<String>()
        val rand = (length / 2..length).random()
        for (i in 0..number) {
            results.add(randomStringByKotlinCollectionRandom(rand))
        }
        return results
    }

    private fun randomStringByKotlinCollectionRandom(length: Int) =
        List(length) { charPool.random() }.joinToString("")

}