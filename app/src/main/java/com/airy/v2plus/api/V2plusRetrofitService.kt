package com.airy.v2plus.api

import android.util.Log
import com.airy.v2plus.App.Companion.getAppContext
import com.airy.v2plus.BuildConfig
import com.airy.v2plus.Config
import com.airy.v2plus.api.cookie.CookieJarImpl
import com.airy.v2plus.api.cookie.PersistentCookieStore
import com.airy.v2plus.util.StringConverter
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit


/**
 * Created by Airy on 2019-07-29
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

class V2plusRetrofitService {

    companion object {

        private const val TAG = "V2plusRetrofitService"

        private val persistentCookieStore = PersistentCookieStore(getAppContext())

        @JvmField
        val headersInterceptor: Interceptor = object :Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val original = chain.request()

                val request = original.newBuilder()
                    .addHeader("Origin", "https://www.v2ex.com")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .header("Host", "www.v2ex.com")
                    .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36")
                    .build()

                return chain.proceed(request)
            }
        }

        @JvmField
        val errorInterceptor = object :Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val response = chain.proceed(chain.request())
                if (response.code != 200) {
                    Log.e(TAG, response.message)
                }
                return response
            }
        }


        @JvmField
        val client: OkHttpClient = OkHttpClient.Builder().let {
            if (BuildConfig.DEBUG) {
                val loggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
                    this.level = HttpLoggingInterceptor.Level.BODY
                }
                it.addInterceptor(loggingInterceptor)
            }
            it.cookieJar(CookieJarImpl(persistentCookieStore))
            it.addInterceptor(headersInterceptor)
            it.addInterceptor(errorInterceptor)
            it.build()
        }



        @JvmField
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(Config.BASE_URL)
            .addConverterFactory(StringConverter())
            .client(client)
            .build()

        @JvmField
        val v2plusApi: V2plusApi = retrofit.create(V2plusApi::class.java)

        fun getV2plusApi(): V2plusApi = v2plusApi

        fun clearCookies(): Boolean = persistentCookieStore.removeAll()
    }
}