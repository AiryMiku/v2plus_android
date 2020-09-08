package com.airy.v2plus.network

import com.airy.v2plus.App
import com.airy.v2plus.BuildConfig
import com.airy.v2plus.Config
import com.airy.v2plus.showToastLong
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

object RequestHelper {

    private const val CONNECT_TIME_OUT = 30L //连接超时时间
    private const val READ_TIME_OUT = 30L
    private val cookieJar =
        PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(App.getAppContext()))

    val networkScope = CoroutineScope(Dispatchers.IO)

    @JvmField
    val headersInterceptor: Interceptor = object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val original = chain.request()

            val request = original.newBuilder().apply {
                header("User-Agent", Config.USER_AGENT)
            }.build()

            return chain.proceed(request)
        }
    }

    @JvmField
    val errorInterceptor = object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val response = chain.proceed(chain.request())
            if (response.code != 200) {
                App.getAppContext().showToastLong("Network error: ${response.message}")
            }
            return response
        }
    }

    @JvmField
    val client: OkHttpClient = OkHttpClient.Builder().apply {
        if (BuildConfig.DEBUG) {
            val loggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
                this.level = HttpLoggingInterceptor.Level.BODY
            }
            addInterceptor(loggingInterceptor)
        }
        connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
        writeTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
        readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
        cookieJar(cookieJar)
        addInterceptor(headersInterceptor)
        addInterceptor(errorInterceptor)
        retryOnConnectionFailure(true)
    }.build()

    internal fun newRequest(useMobile: Boolean = false): Request.Builder {
        val ua = if (useMobile) {
            Config.USER_AGENT_ANDROID
        } else {
            Config.USER_AGENT
        }
        return Request.Builder().apply {
            header("User-Agent", ua)
        }
    }

    fun getCaptchaImageUrl(once: String): String = "${Config.BASE_URL}/_captcha?once=$once"

    fun clearCookieAll() = cookieJar.clear()

    fun clearCookieSession() = cookieJar.clearSession()

    fun isCookieExpired(): Boolean {
        val url = Config.BASE_URL.toHttpUrlOrNull()
        url?.let {
            val cookies = cookieJar.loadForRequest(it)
            for (c in cookies) {
                if (c.name == "A2") {
                    return System.currentTimeMillis() > c.expiresAt
                }
            }
        }
        return true
    }
}