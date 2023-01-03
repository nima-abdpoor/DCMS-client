package com.nima.network.manager.request

import android.content.Context
import java.io.*
import java.net.HttpURLConnection
import java.net.URL


class FileUploaderHttpRequestBuilder(requestURL: String?) {
    private val httpConn: HttpURLConnection
    private val request: DataOutputStream
    private val boundary = "*****"
    private val crlf = "\r\n"
    private val twoHyphens = "--"

    /**
     * Adds a form field to the request
     *
     * @param name  field name
     * @param value field value
     */
    @Throws(IOException::class)
    fun addFormField(name: String, value: String) {
        request.writeBytes(twoHyphens + boundary + crlf)
        request.writeBytes("Content-Disposition: form-data; name=\"$name\"$crlf")
        request.writeBytes("Content-Type: text/plain; charset=UTF-8$crlf")
        request.writeBytes(crlf)
        request.writeBytes(value + crlf)
        request.flush()
    }

    /**
     * Adds a upload file section to the request
     *
     * @param fieldName  name attribute in <input type="file" name="..."></input>
     * @param uploadFile a File to be uploaded
     * @throws IOException
     */

    @Throws(IOException::class)
    fun addFilePart(fieldName: String, appContext: Context, fileName: String) {
        request.writeBytes(twoHyphens + boundary + crlf)
        request.writeBytes(
            "Content-Disposition: form-data; name=\"" +
                    fieldName + "\";filename=\"" +
                    fileName + "\"" + crlf
        )
        request.writeBytes(crlf)
        var text: String
        appContext.openFileInput(fileName).use { stream ->
            text = stream.bufferedReader().use {
                it.readText()
            }
        }
        request.write(text.toByteArray())
    }

    /**
     * Completes the request and receives response from the server.
     *
     * @return a list of Strings as response in case the server returned
     * status OK, otherwise an exception is thrown.
     * @throws IOException
     */
    @Throws(IOException::class)
    fun finish(): String {
        var response: String = ""
        request.writeBytes(crlf)
        request.writeBytes(
            (twoHyphens + boundary +
                    twoHyphens + crlf)
        )
        request.flush()
        request.close()

        // checks server's status code first
        val status: Int = httpConn.responseCode
        if (status == HttpURLConnection.HTTP_OK) {
            val responseStream: InputStream = BufferedInputStream(httpConn.inputStream)
            val responseStreamReader = BufferedReader(InputStreamReader(responseStream))
            var line: String? = ""
            val stringBuilder = StringBuilder()
            while ((responseStreamReader.readLine().also { line = it }) != null) {
                stringBuilder.append(line).append("\n")
            }
            responseStreamReader.close()
            response = stringBuilder.toString()
            httpConn.disconnect()
        } else {
            throw IOException("Server returned non-OK status: $status")
        }
        return response
    }

    /**
     * This constructor initializes a new HTTP POST request with content type
     * is set to multipart/form-data
     *
     * @param requestURL
     * @throws IOException
     */
    init {

        // creates a unique boundary based on time stamp
        val url = URL(requestURL)
        httpConn = url.openConnection() as HttpURLConnection
        httpConn.useCaches = false
        httpConn.doOutput = true // indicates POST method
        httpConn.doInput = true
        httpConn.requestMethod = "POST"
        httpConn.setRequestProperty("Connection", "Keep-Alive")
        httpConn.setRequestProperty("Cache-Control", "no-cache")
        httpConn.setRequestProperty(
            "Content-Type", "multipart/form-data;boundary=$boundary"
        )
        request = DataOutputStream(httpConn.outputStream)
    }
}