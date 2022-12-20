package com.nima.dcms

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
        Assert.assertEquals(true, true)
    }

}