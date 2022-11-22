package com.nima.network

import com.nima.common.mapper.toConfigBodyMapper
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class StringToConfigBodyMapperKtTest {
    private var sampleData: String = ""

    init {
        sampleData = createSampleConfigBody()
    }

    @Test
    fun testStringToConfigBody() {
        val configBody = sampleData.toConfigBodyMapper()
        assertNotNull(configBody)
        assertNotNull(configBody.validRequestUrls)
        assertNotNull(configBody.urlIdFirst)
        assertNotNull(configBody.urlIdSecond)
        assertNotNull(configBody.syncType)
        assertNotNull(configBody.isLive)
        assertEquals(2, configBody.validRequestUrls?.size)
        configBody.validRequestUrls?.let {
            assertEquals("http://salam", it[0])
            assertEquals("https://requestts", it[1])
        }
        configBody.urlIdFirst?.let {
            assertEquals(1254444L, it[0].ids)
            assertEquals(258774111L, it[1].ids)
        }
        configBody.urlIdSecond?.let {
            assertEquals(125477L, it[0].ids)
            assertEquals(0L, it[0].startIndex)
            assertEquals(1L, it[0].finishIndex)
            assertEquals("aksdlfja", it[0].regex)
            assertEquals(158777L, it[1].ids)
            assertEquals(10L, it[1].startIndex)
            assertEquals(22L, it[1].finishIndex)
            assertEquals("askldjf", it[1].regex)
        }
        assertEquals(false, configBody.isLive)
        assertEquals("1", configBody.syncType)
    }

    private fun createSampleConfigBody(): String {
        return "{\"validRequestUrls\":[\"http://salam\",\"https://requestts\"],\"urlIdFirst\":[1254444,258774111],\"urlIdSecond\":[{\"urlId\":125477,\"regex\":\"aksdlfja\",\"startIndex\":0,\"finishIndex\":1},{\"urlId\":158777,\"regex\":\"askldjf\",\"startIndex\":10,\"finishIndex\":22}],\"isLive\":false,\"syncType\":\"1\"}"
    }
}