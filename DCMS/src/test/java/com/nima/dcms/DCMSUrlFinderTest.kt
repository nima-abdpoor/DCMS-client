package com.nima.dcms

import com.nima.common.database.entitty.Regex
import com.nima.common.database.entitty.URLIdSecond
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
    private val numberOfSecondUrlHashes = 20
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
            val result =
                finder.searchInUrlFirst(hash = allUrls[allUrls.size - (i + 1)], firstUlrs = allUrls)
            val timeResult = System.currentTimeMillis() - t1
            Assert.assertEquals(allUrls.any { it == allUrls[allUrls.size - (i + 1)] }, result)
            results.add(timeResult)
        }
        println(results.average())
    }

    @Test
    fun searchInUrlSecondTest() {
        val data = createUrlSecondMockData()
        val urls = generateString(numberOfSecondUrlHashes, 105)
        getTestUrlsForUrlSecond().forEach { pair ->
            val actualResult = finder.searchInUrlSecond(
                url = pair.first,
                hash = converter.convert(pair.first),
                urlHashSecond = data.first,
                regexes = data.second
            )
            Assert.assertEquals(pair.second, actualResult)
        }

    }

    private fun createUrlSecondMockData(): Pair<List<URLIdSecond>, List<Regex>> {
        val urlSeconds = mutableListOf<URLIdSecond>()
        val regexes = mutableListOf(
            Regex(id = 0, urlId = 0, regex = "([KLD-]+)([\\d+]+)", startIndex = 4, finishIndex = 0),
            Regex(id = 1, urlId = 1, regex = "([KLD-]+)([\\d+]+)", startIndex = 4, finishIndex = 0),
            Regex(id = 2, urlId = 2, regex = "([\\d]+)", startIndex = 4, finishIndex = 0),
            Regex(id = 3, urlId = 3, regex = "([\\d]+)", startIndex = 4, finishIndex = 0),
            Regex(id = 4, urlId = 4, regex = "([\\d]+)", startIndex = 4, finishIndex = 0),
            Regex(id = 5, urlId = 5, regex = "([\\d]+)/([\\d]+)", startIndex = 4, finishIndex = 0),
            Regex(id = 6, urlId = 6, regex = "([\\w]+)", startIndex = 3, finishIndex = 0),
            Regex(id = 7, urlId = 7, regex = "([\\w]+)", startIndex = 3, finishIndex = 0),
        )
        getTestUrlIncludedRegex().forEachIndexed { index, s ->
            urlSeconds.add(URLIdSecond(id = index.toLong(), urlHash = converter.convert(s)))
        }
        return Pair<List<URLIdSecond>, List<Regex>>(urlSeconds, regexes)
    }

    private fun getTestUrlsForUrlSecond(): ArrayList<Pair<String, Boolean>> {
        return arrayListOf(
            Pair("https://jeiran.adanic.me/browse/KLD-2992", true),
            Pair("https://jeiran.adanic.me/browse/KLD-20252/salam", true),
            Pair("https://github.com/nima-abdpoor/123", true),
            Pair("https://github.com/nima-abdpoor/123/asldkf", true),
            Pair("https://github.com/nima-abdpoor/123/1111/", true),
            Pair("https://github.com/nima-abdpoor/123/25N/", true),
            Pair("https://github.com/ahmad", true),
            Pair("https://github.com/ahmad/nima", true),
        )
    }

    //urls that can be retrieved from server. this will be converter to hash
    private fun getTestUrlIncludedRegex(): ArrayList<String> {
        return arrayListOf(
            "https://jeiran.adanic.me/browse/*" +
                    "https://jeiran.adanic.me/browse/*/salam" +
                    "https://github.com/nima-abdpoor/*" +
                    "https://github.com/nima-abdpoor/*/asldkf" +
                    "https://github.com/nima-abdpoor/*/1111/" +
                    "https://github.com/nima-abdpoor/*/25N/" +
                    "https://github.com/*" +
                    "https://github.com/*/nima"
        )
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