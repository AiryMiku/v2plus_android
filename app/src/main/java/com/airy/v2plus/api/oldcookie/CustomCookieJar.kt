package com.airy.v2plus.api.oldcookie

import android.util.Log
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.internal.delimiterOffset
import okhttp3.internal.http2.Http2Reader.Companion.logger
import okhttp3.internal.trimSubstring
import java.io.IOException
import java.net.CookieHandler
import java.util.*
import java.util.Collections.singletonMap
import java.util.logging.Level.WARNING
import kotlin.collections.ArrayList


/**
 * Created by Airy on 2019-09-16
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

class CustomCookieJar(private val cookieHandler: CookieHandler): CookieJar {

    companion object{
        const val TAG = "CustomCookieJar"
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        val headers: Map<String, List<String>> = Collections.emptyMap()
        val cookieHeaders: Map<String, List<String>>
        try {
            cookieHeaders = cookieHandler.get(url.toUri(), headers)
        } catch (e: IOException) {
            logger.log(WARNING, "Loading cookies failed for " + url.resolve("/..."), e);
            return emptyList()
        }

        val cookies = ArrayList<Cookie>()
        for (entry in cookieHeaders.entries) {
            val key = entry.key
            if ("Cookie".equals(key, true) || "Cookie2".equals(key, true) && entry.value.isNotEmpty()) {
                for (header in entry.value) {
                    cookies.addAll(decodeHeaderAsJavaNetCookies(url, header))
                }
            }
        }

        return cookies
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        val cookieStrings = ArrayList<String>()
        for (cookie in cookies) {
            cookieStrings.add(cookie.toString())
            Log.d(TAG, "cookie save from response->$cookie")
        }
        val multimap = singletonMap("Set-Cookie", cookieStrings)
        try {
            cookieHandler.put(url.toUri(), multimap.toMap())
        } catch (e: IOException) {
            logger.log(WARNING, "Saving cookies failed for " + url.resolve("/..."), e)
        }

    }

    private fun decodeHeaderAsJavaNetCookies(url: HttpUrl, header: String): List<Cookie> {
        val result = ArrayList<Cookie>()
        var pos = 0
        val limit = header.length
        var pairEnd: Int
        while (pos < limit) {
            pairEnd = header.delimiterOffset(";,", pos, limit)
            val equalsSign = header.delimiterOffset('=', pos, pairEnd)
            val name = header.trimSubstring(pos, equalsSign)
            if (name.startsWith("$")) {
                pos = pairEnd + 1
                continue
            }

            // We have either name=value or just a name.
            var value = if (equalsSign < pairEnd)
                header.trimSubstring(equalsSign + 1, pairEnd)
            else
                ""

            // If the value is "quoted", drop the quotes.
            if (value.startsWith("\"") && value.endsWith("\"")) {
                value = value.substring(1, value.length - 1)
            }

            result.add(
                Cookie.Builder()
                    .name(name)
                    .value(value)
                    .domain(url.host)
                    .build()
            )
            pos = pairEnd + 1
        }
        return result
    }
}