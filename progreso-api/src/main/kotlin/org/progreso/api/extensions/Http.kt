package org.progreso.api.extensions

import org.progreso.api.Api
import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.URI

private const val USER_AGENT =
    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36 Edg/117.0.2045.47"

fun request(url: String, method: String, data: String, headers: Map<String, String>): String {
    val httpConnection = URI(url).toURL().openConnection() as HttpURLConnection

    httpConnection.requestMethod = method
    httpConnection.connectTimeout = 2000
    httpConnection.readTimeout = 10000

    httpConnection.setRequestProperty("User-Agent", USER_AGENT)

    for ((key, value) in headers) {
        httpConnection.setRequestProperty(key, value)
    }

    httpConnection.instanceFollowRedirects = true
    httpConnection.doOutput = true

    if (data.isNotEmpty()) {
        val dataOutputStream = DataOutputStream(httpConnection.outputStream)
        dataOutputStream.writeBytes(data)
        dataOutputStream.flush()
    }

    httpConnection.connect()

    return httpConnection.inputStream.reader().readText()
}

inline fun <reified T> getRequest(
    url: String,
    headers: Map<String, String> = emptyMap()
): T {
    val response = request(url, "GET", "", headers)

    return if (T::class == String::class) {
        response as T
    } else {
        Api.GSON.fromJson(response, T::class.java)
    }
}

inline fun <reified T> postRequest(
    url: String,
    data: String,
    headers: Map<String, String> = emptyMap()
): T {
    val response = request(url, "POST", data, headers)

    return if (T::class == String::class) {
        response as T
    } else {
        Api.GSON.fromJson(response, T::class.java)
    }
}