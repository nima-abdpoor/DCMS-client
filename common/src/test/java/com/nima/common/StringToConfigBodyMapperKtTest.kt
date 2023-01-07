package com.nima.common

import com.nima.common.mapper.toConfigBodyMapper
import com.nima.common.model.ConfigBody
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
        val configBody = sampleData.toConfigBodyMapper<ConfigBody>()
        assertNotNull(configBody)
        assertNotNull(configBody.validRequestUrls)
        assertNotNull(configBody.urlIdFirst)
        assertNotNull(configBody.urlIdSecond)
        assertNotNull(configBody.networkType)
        assertNotNull(configBody.isLive)
        assertEquals(2, configBody.validRequestUrls?.size)
        configBody.validRequestUrls?.let {
            assertEquals("http://salam", it[0])
            assertEquals("https://requestts", it[1])
        }
        configBody.urlIdFirst?.let {
            assertEquals(1254444L, it[0].id)
            assertEquals(258774111L, it[1].id)
        }
        configBody.urlIdSecond?.let {
            assertEquals(125477L, it[0].id)
            assertEquals("aksdlfja", it[0].regex)
            assertEquals("askldjf", it[1].regex)
        }
        assertEquals(false, configBody.isLive)
        assertEquals("1", configBody.networkType)
    }

    private fun createSampleConfigBody(): String {
        return "{\"validRequestUrls\":[\"http://salam\",\"https://requestts\"],\"urlIdFirst\":[1254444,258774111],\"urlIdSecond\":[{\"urlId\":125477,\"regex\":\"aksdlfja\",\"startIndex\":0,\"finishIndex\":1},{\"urlId\":158777,\"regex\":\"askldjf\",\"startIndex\":10,\"finishIndex\":22}],\"isLive\":false,\"syncType\":\"1\"}"
    }
}