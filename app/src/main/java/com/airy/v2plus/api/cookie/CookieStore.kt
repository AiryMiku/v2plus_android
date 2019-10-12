package com.airy.v2plus.api.cookie

import okhttp3.Cookie
import okhttp3.HttpUrl


/**
 * Created by Airy on 2019-10-10
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

interface CookieStore {

    fun add(httpUrl: HttpUrl, cookie: Cookie)

    fun add(httpUrl: HttpUrl, cookies: List<Cookie>)

    fun getCookies(httpUrl: HttpUrl): List<Cookie>

    fun getAllCookies(): List<Cookie>

    fun remove(httpUrl: HttpUrl, cookie: Cookie): Boolean

    fun removeAll(): Boolean
}