package com.nima.dcms

import com.nima.dcms.interceptor.DCMSUrlFinder
import com.nima.dcms.urlconverter.CR32URLConverter
import com.nima.dcms.urlconverter.URLConverter
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class DCMSUrlFinderTest {
    private lateinit var converter: URLConverter
    private val charPool: List<Char> = ('a'..'z') + ('A'..'Z')
    private val numberOfUrlHashes = 10
    private val finder = DCMSUrlFinder()
    private var allUrls = ArrayList<Long>()
    private var firstUrls = ArrayList<Long>()

    @Before
    fun initValues() {
        converter = CR32URLConverter()
        allUrls = generateUrlHash(numberOfUrlHashes) as ArrayList<Long>
        firstUrls = allUrls.filter { it % 2 == 0L } as ArrayList<Long>
    }

    @Test
    fun searchInUrlFirstTest() {
        firstUrls.forEach {
            println(it)
        }
        allUrls.forEachIndexed { index, l ->
            val result = finder.searchInUrlFirst(hash = l, firstUlrs = firstUrls)
            Assert.assertEquals(firstUrls.any { it == l }, result)
        }
    }

    @Test
    fun searchInUrlFirstTimeTest() {
        //find url among ONE MILLION urls.
        allUrls = generateUrlHash(1_000_000) as ArrayList<Long>
        val results = arrayListOf<Long>()
        for (i in 1_000 downTo 0) {
            val t1 = System.currentTimeMillis()
            val result = finder.searchInUrlFirst(hash = allUrls[allUrls.size - (i + 1)], firstUlrs = allUrls)
            val timeResult = System.currentTimeMillis() - t1
            Assert.assertEquals(allUrls.any { it == allUrls[allUrls.size - (i + 1)] }, result)
            results.add(timeResult)
        }
        println(results.average())
    }

    private fun generateUrlHash(number: Int): List<Long> {
        val res = generateString(number, 100)
        return res.map { converter.convert(it) }
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