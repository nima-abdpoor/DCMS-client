package com.nima.dcms

import com.nima.common.database.entitty.Regex
import com.nima.common.database.entitty.URLIdSecond
import com.nima.dcms.interceptor.DCMSUrlFinder
import com.nima.dcms.urlconverter.CR32URLConverter
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(JUnitParamsRunner::class)
class DCMSUrlFinderTest {
    private val converter = CR32URLConverter()
    private val charPool: List<Char> = ('a'..'z') + ('A'..'Z')
    private val numberOfUrlHashes = 10
    private val finder = DCMSUrlFinder()
    private var allUrls = ArrayList<Long>()
    private var firstUrls = ArrayList<Long>()

    @Before
    fun initValues() {
        allUrls = generateUrlHash(numberOfUrlHashes) as ArrayList<Long>
        firstUrls = allUrls.filter { it % 2 == 0L } as ArrayList<Long>
    }

    @Test
    fun searchInUrlFirstTest() {
        allUrls.forEachIndexed { _, l ->
            val result = finder.searchInUrlFirst(hash = l, firstUrls = firstUrls)
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
                finder.searchInUrlFirst(hash = allUrls[allUrls.size - (i + 1)], firstUrls = allUrls)
            val timeResult = System.currentTimeMillis() - t1
            Assert.assertEquals(allUrls.any { it == allUrls[allUrls.size - (i + 1)] }, result)
            results.add(timeResult)
        }
    }

    @Test
    @Parameters(method = "getUserAPICallsMockData")
    fun urlFinderClassShouldFindTheSecondUrl(
        userUrl: String
    ) {
        val actualResult = finder.searchInUrlSecond(userUrl, getUrlSWithStarsThatIsMockAsServerUrls().toList(), getRegexMockDataForUrlSecond())
        Assert.assertEquals(true, actualResult)
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

    private fun getRegexMockDataForUrlSecond(): List<Regex> {
        return listOf(
            Regex(id = 0, urlId = 0, regex = "([KLD-]+)([\\d+]+)", startIndex = 31, finishIndex = 0),
            Regex(id = 1, urlId = 1, regex = "([KLD-]+)([\\d+]+)", startIndex = 31, finishIndex = 0),
            Regex(id = 2, urlId = 2, regex = "([\\d]+)", startIndex = 31, finishIndex = 0),
            Regex(id = 3, urlId = 3, regex = "([\\d]+)", startIndex = 31, finishIndex = 0),
            Regex(id = 4, urlId = 4, regex = "([\\d]+)", startIndex = 31, finishIndex = 0),
            Regex(id = 5, urlId = 5, regex = "([\\d]+)", startIndex = 31, finishIndex = 0),
            Regex(id = 6, urlId = 6, regex = "([\\w]+)", startIndex = 18, finishIndex = 0),
            Regex(id = 7, urlId = 7, regex = "([\\w]+)", startIndex = 18, finishIndex = 0),
        )
    }

    private fun getUrlSWithStarsThatIsMockAsServerUrls(): Array<URLIdSecond> {
        return arrayOf(
            URLIdSecond(id = 0, urlHash = converter.convert("https://jeiran.adanic.me/browse/*/")),
            URLIdSecond(id = 1, urlHash = converter.convert("https://jeiran.adanic.me/browse/*/salam/")),
            URLIdSecond(id = 2, urlHash = converter.convert("https://github.com/nima-abdpoor/*/")),
            URLIdSecond(id = 3, urlHash = converter.convert("https://github.com/nima-abdpoor/*/asldkf/")),
            URLIdSecond(id = 4, urlHash = converter.convert("https://github.com/nima-abdpoor/*/1111/")),
            URLIdSecond(id = 5, urlHash = converter.convert("https://github.com/nima-abdpoor/*/25N/")),
            URLIdSecond(id = 6, urlHash = converter.convert("https://github.com/*/")),
            URLIdSecond(id = 7, urlHash = converter.convert("https://github.com/*/nima/")),
        )
    }

    fun getUserAPICallsMockData(): Array<Any> {
        return arrayOf(
            "https://jeiran.adanic.me/browse/KLD-2992/",
            "https://jeiran.adanic.me/browse/KLD-20252/salam/",
            "https://github.com/nima-abdpoor/123/",
            "https://github.com/nima-abdpoor/123/asldkf/",
            "https://github.com/nima-abdpoor/123/1111/",
            "https://github.com/nima-abdpoor/123/25N/",
            "https://github.com/ahmad/",
            "https://github.com/ahmad/nima/"
        )
    }
}