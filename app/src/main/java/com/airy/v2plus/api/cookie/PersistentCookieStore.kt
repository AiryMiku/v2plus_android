package com.airy.v2plus.api.cookie

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import android.util.Log
import okhttp3.Cookie
import okhttp3.HttpUrl
import okhttp3.internal.and
import java.io.*
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


/**
 * Created by Airy on 2019-10-10
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */
@JvmSuppressWildcards
class PersistentCookieStore(context: Context): CookieStore {

    private val TAG = "PersistentCookieStore"
    private val COOKIE_PREFS = "CookiePrefsFile"
    private val HOST_NAME_PREFIX = "host_"
    private val COOKIE_NAME_PREFIX = "cookie_"
    private var cookies: HashMap<String, ConcurrentHashMap<String, Cookie>>
    private var  cookiePrefs: SharedPreferences
    private var omitNonPersistentCookies: Boolean = false

    init {
        cookiePrefs = context.getSharedPreferences(COOKIE_PREFS, Context.MODE_PRIVATE)
        cookies = HashMap()

        val tempCookieMap = HashMap<Any, Any>(cookiePrefs.all)
        for (key in tempCookieMap.keys) {
            if (key !is String || !key.contains(HOST_NAME_PREFIX)) {
                continue
            }
            val cookieNames = tempCookieMap[key] as String
            if (TextUtils.isEmpty(cookieNames)) {
                continue
            }
            if (!cookies.containsKey(key)) {
                cookies[key] = ConcurrentHashMap()
            }

            val cookieNameArray = cookieNames.split(",")
            for (name in cookieNameArray) {
                val encodeCookie = cookiePrefs.getString(COOKIE_NAME_PREFIX + name, null)
                    ?: continue

                val decodeCookie = decodeCookie(encodeCookie)
                if (decodeCookie != null) {
                    cookies[key]?.put(name, decodeCookie)
                }
            }
        }
        tempCookieMap.clear()

        clearExpired()
    }
    override fun add(httpUrl: HttpUrl, cookie: Cookie) {
        if (omitNonPersistentCookies && !cookie.persistent) {
            return
        }

        val name = cookieName(cookie)
        val hostKey = hostName(httpUrl)

        if (!cookies.containsKey(hostKey)) {
            cookies[hostKey] = ConcurrentHashMap()
        }
        cookies[hostKey]?.put(name, cookie)

        // save into persistent store
        val prefsWriter = cookiePrefs.edit()
        prefsWriter.putString(hostKey, TextUtils.join(",", cookies[hostKey]!!.keys))
        prefsWriter.putString(COOKIE_NAME_PREFIX + name, encodeCookie(SerializableCookie(cookie)))
        prefsWriter.apply()
    }

    override fun add(httpUrl: HttpUrl, cookies: List<Cookie>) {
        for (cookie in cookies) {
            if (isCookieExpired(cookie)) {
                continue
            }
            add(httpUrl, cookie)
        }
    }

    override fun getCookies(httpUrl: HttpUrl): List<Cookie> {
        return get(hostName(httpUrl))
    }

    override fun getAllCookies(): List<Cookie> {
        val result = ArrayList<Cookie>()
        for (hostKey in cookies.keys) {
            result.addAll(get(hostKey))
        }
        return result
    }

    override fun remove(httpUrl: HttpUrl, cookie: Cookie): Boolean {
        return remove(hostName(httpUrl), cookie)
    }

    override fun removeAll(): Boolean {
        val prefsWriter = cookiePrefs.edit()
        prefsWriter.clear()
        prefsWriter.apply()
        cookies.clear()
        return true
    }

    private fun remove(hostKey: String, cookie: Cookie): Boolean {
        val name = cookieName(cookie)
        if (cookies.containsKey(hostKey) && cookies[hostKey]?.containsKey(name)!!) {
            cookies[hostKey]?.remove(name)

            val prefsWriter = cookiePrefs.edit()
            prefsWriter.remove(COOKIE_NAME_PREFIX + name)
            prefsWriter.putString(hostKey, TextUtils.join(",", cookies[hostKey]?.keys))
            prefsWriter.apply()
            return true
        }
        return false
    }

    // get cookies
    private fun get(hostKey: String): List<Cookie> {
        val result = ArrayList<Cookie>()

        if (cookies.containsKey(hostKey)) {
            val tempCookies = cookies[hostKey]?.values

            if (tempCookies != null) {
                for (cookie in tempCookies) {
                    if (isCookieExpired(cookie)) {
                        remove(hostKey, cookie)
                    } else {
                        result.add(cookie)
                    }
                }
            }

        }
        return result
    }

    private fun clearExpired() {
        val prefsWriter = cookiePrefs.edit()

        for (key in cookies.keys) {
            var changeFlag = false

            for (entry in cookies[key]!!.entries) {
                val name = entry.key
                val cookie = entry.value
                if (isCookieExpired(cookie)) {
                    cookies[key]?.remove(name)
                    prefsWriter.remove(COOKIE_NAME_PREFIX + name)
                    changeFlag = true
                }
            }

            if (changeFlag) {
                prefsWriter.putString(key, TextUtils.join(",", cookies.keys))
            }
        }
        prefsWriter.apply()
    }

    private fun setOmitNonPersistentCookies(omitNonPersistentCookies: Boolean) {
        this.omitNonPersistentCookies = omitNonPersistentCookies
    }

    private fun isCookieExpired(cookie: Cookie): Boolean {
        return cookie.expiresAt < System.currentTimeMillis()
    }

    private fun hostName(httpUrl: HttpUrl): String {
        return if (httpUrl.host.startsWith(HOST_NAME_PREFIX)) {
            httpUrl.host
        } else {
            HOST_NAME_PREFIX + httpUrl.host
        }
    }

    private fun cookieName(cookie: Cookie): String {
        return cookie.name + cookie.domain
    }

    private fun encodeCookie(cookie: SerializableCookie?): String? {
        if (cookie == null)
            return null
        val os = ByteArrayOutputStream()
        try {
            val outputStream = ObjectOutputStream(os)
            outputStream.writeObject(cookie)
        } catch (e: IOException) {
            Log.d(TAG, "IOException in encodeCookie", e)
            return null
        }

        return byteArrayToHexString(os.toByteArray())
    }

    private fun decodeCookie(cookieString: String): Cookie? {
        val bytes = hexStringToByteArray(cookieString)
        val byteArrayInputStream = ByteArrayInputStream(bytes)
        var cookie: Cookie? = null
        try {
            val objectInputStream = ObjectInputStream(byteArrayInputStream)
            cookie = (objectInputStream.readObject() as SerializableCookie).getCookie()
        } catch (e: IOException) {
            Log.d(TAG, "IOException in decodeCookie", e)
        } catch (e: ClassNotFoundException) {
            Log.d(TAG, "ClassNotFoundException in decodeCookie", e);
        }
        return cookie
    }

    private fun byteArrayToHexString(bytes: ByteArray): String {
        val sb = StringBuilder(bytes.size * 2)
        for (b in bytes) {
            val v = b.and(0xff)
            if (v < 16) {
                sb.append('0')
            }
            sb.append(Integer.toHexString(v))
        }
        return sb.toString().toUpperCase(Locale.US)
    }

    private fun hexStringToByteArray(hexString: String): ByteArray {
        val len = hexString.length
        val bytes = ByteArray(len.shr(1))
        var i = 0
        while (i<len) {
            bytes[i.shr(1)] = (Character.digit(hexString[i], 16).shl(4) +
                        Character.digit(hexString[i + 1], 16)).toByte()
            i+=2
        }
        return bytes
    }
}