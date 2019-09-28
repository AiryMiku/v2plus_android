package com.airy.v2plus.api.cookie

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import android.util.Log
import java.io.*
import java.net.CookieStore
import java.net.HttpCookie
import java.net.URI
import java.net.URISyntaxException
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.experimental.and


/**
 * Created by Airy on 2019-09-16
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

class PersistentCookieStore(context: Context) : CookieStore {

    private val LOG_TAG = "PersistentCookieStore"
    private val COOKIE_PREFS = "CookiePrefsFile"
    private val COOKIE_NAME_PREFIX = "cookie_"
    private val mCookiePrefs: SharedPreferences
    private var mCookies: HashMap<String, ConcurrentHashMap<String, HttpCookie>>


    init {
        mCookiePrefs = context.getSharedPreferences(COOKIE_PREFS, Context.MODE_PRIVATE)
        mCookies = HashMap()
        val prefsMap = mCookiePrefs.all
        for ((key, value) in prefsMap) {
            if (value != null && !(value as String).startsWith(COOKIE_NAME_PREFIX)) {
                val cookieNames = TextUtils.split(value, ",")
                for (name in cookieNames) {
                    val encodedCookie = mCookiePrefs.getString(COOKIE_NAME_PREFIX + name, null)
                    if (encodedCookie != null) {
                        val decodedCookie = decodeCookie(encodedCookie)
                        if (decodedCookie != null) {
                            if (!mCookies.containsKey(key))
                                mCookies[key] = ConcurrentHashMap()
                            mCookies[key]?.put(name, decodedCookie)
                        }
                    }
                }

            }
        }
    }

    override fun add(uri: URI, cookie: HttpCookie) {
        // Save cookie into local store, or remove if expired
        if (!cookie.hasExpired()) {
            if (!mCookies.containsKey(cookie.domain))
                mCookies[cookie.domain] = ConcurrentHashMap()
            mCookies[cookie.domain]?.put(cookie.name, cookie)
        } else {
            if (mCookies.containsKey(cookie.domain))
                mCookies[cookie.domain]?.remove(cookie.domain)
        }

        TokenCache.saveToken(cookie.value)
        // Save cookie into persistent store
        val prefsWriter = mCookiePrefs.edit()
        prefsWriter.putString(cookie.domain, TextUtils.join(",", mCookies[cookie.domain]?.keys))
        Log.d(LOG_TAG, "cookie keys->${mCookies[cookie.domain]?.keys}")
        val encodeCookie = encodeCookie(SerializableHttpCookie(cookie))
        prefsWriter.putString(COOKIE_NAME_PREFIX + cookie.name, encodeCookie)
        Log.d(LOG_TAG, "encodeCookie-> $encodeCookie")
        prefsWriter.apply()
    }

    private fun getCookieToken(cookie: HttpCookie): String {
        return cookie.name + cookie.domain
    }

    override operator fun get(uri: URI): List<HttpCookie> {
        val ret = ArrayList<HttpCookie>()
        for (key in mCookies.keys) {
            if (uri.host.contains(key)) {
                mCookies[key]?.values?.let {
                    ret.addAll(it.toList())
                }
            }
        }
        return ret
    }

    override fun removeAll(): Boolean {
        val prefsWriter = mCookiePrefs.edit()
        prefsWriter.clear()
        prefsWriter.apply()
        mCookies.clear()
        return true
    }


    override fun remove(uri: URI, cookie: HttpCookie): Boolean {
        val name = getCookieToken(cookie)

        if (mCookies.containsKey(uri.host) && mCookies[uri.host]!!.containsKey(name)) {
            mCookies[uri.host]?.remove(name)

            val prefsWriter = mCookiePrefs.edit()
            if (mCookiePrefs.contains(COOKIE_NAME_PREFIX + name)) {
                prefsWriter.remove(COOKIE_NAME_PREFIX + name)
            }
            prefsWriter.putString(
                uri.host,
                TextUtils.join(",", mCookies[uri.host]?.keys)
            )
            prefsWriter.apply()
            return true
        } else {
            return false
        }
    }

    override fun getCookies(): List<HttpCookie> {
        val ret = ArrayList<HttpCookie>()
        for (key in mCookies.keys) {
            mCookies[key]?.let {
                ret.addAll(it.values)
                Log.d(LOG_TAG, key + " : " + mCookies.keys)
            }
        }
        return ret
    }

    override fun getURIs(): List<URI> {
        val ret = ArrayList<URI>()
        for (key in mCookies.keys)
            try {
                ret.add(URI(key))
            } catch (e: URISyntaxException) {
                e.printStackTrace()
            }
        return ret
    }

    /**
     * Serializes Cookie object into String
     *
     * @param cookie cookie to be encoded, can be null
     * @return cookie encoded as String
     */
    private fun encodeCookie(cookie: SerializableHttpCookie?): String? {
        if (cookie == null)
            return null
        val os = ByteArrayOutputStream()
        try {
            val outputStream = ObjectOutputStream(os)
            outputStream.writeObject(cookie)
        } catch (e: IOException) {
            Log.d(LOG_TAG, "IOException in encodeCookie", e)
            return null
        }

        return byteArrayToHexString(os.toByteArray())
    }

    /**
     * Returns cookie decoded from cookie string
     *
     * @param cookieString string of cookie as returned from http request
     * @return decoded cookie or null if exception occured
     */
    private fun decodeCookie(cookieString: String): HttpCookie? {
        val bytes = hexStringToByteArray(cookieString)
        val byteArrayInputStream = ByteArrayInputStream(bytes)
        var cookie: HttpCookie? = null
        var objectInputStream: ObjectInputStream? = null
        try {
            objectInputStream = ObjectInputStream(byteArrayInputStream)
            cookie = (objectInputStream.readObject() as SerializableHttpCookie).getCookies()
        } catch (e: IOException) {
            Log.d(LOG_TAG, "IOException in decodeCookie", e)
        } catch (e: ClassNotFoundException) {
            Log.d(LOG_TAG, "ClassNotFoundException in decodeCookie", e)
        } finally {
            if (objectInputStream != null) {
                try {
                    objectInputStream.close()
                } catch (e: Exception) {
                    Log.d(LOG_TAG, "Stream not closed in decodeCookie", e);
                }
            }
        }
        return cookie
    }

    /**
     * Using some super basic byte array <-> hex conversions so we don't have to rely on any
     * large Base64 libraries. Can be overridden if you like!
     *
     * @param bytes byte array to be converted
     * @return string containing hex values
     */
    private fun byteArrayToHexString(bytes: ByteArray): String {
        val sb = StringBuilder(bytes.size * 2)
        for (element in bytes) {
            val v = element.and(0xff.toByte())
            if (v < 16) {
                sb.append('0')
            }
            sb.append(Integer.toHexString(v.toInt()))
        }
        return sb.toString().toUpperCase(Locale.US)
    }

    /**
     * Converts hex values from strings to byte array
     *
     * @param hexString string of hex-encoded values
     * @return decoded byte array
     */
    private fun hexStringToByteArray(hexString: String): ByteArray {
        Log.d(LOG_TAG, "hexString->$hexString\n" +
                "Length->${hexString.length}")
        var len = hexString.length
        var inHex = hexString
        if (len % 2 == 1) {
            inHex = "0$hexString"
            len = inHex.length
        }
        val b = ByteArray(len / 2)
        var i = 0
        while (i < len) {
            b[i / 2] = ((Character.digit(inHex[i], 16) shl 4) + Character.digit(inHex[i + 1], 16)).toByte()
            i += 2
        }
        return b
    }
}