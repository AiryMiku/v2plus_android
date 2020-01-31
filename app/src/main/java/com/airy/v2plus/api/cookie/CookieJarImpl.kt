package com.airy.v2plus.api.cookie

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl


/**
 * Created by Airy on 2019-10-11
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

class CookieJarImpl(private val cookieStore: CookieStore)
    : CookieJar {

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cookieStore.getCookies(url)
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        cookieStore.add(url, cookies)
    }

}