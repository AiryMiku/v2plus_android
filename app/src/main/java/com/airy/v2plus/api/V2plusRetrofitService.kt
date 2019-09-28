package com.airy.v2plus.api

import com.airy.v2plus.AppConfigurations
import com.airy.v2plus.V2plusApp.Companion.getAppContext
import com.airy.v2plus.api.cookie.CustomCookieJar
import com.airy.v2plus.api.cookie.PersistentCookieStore
import com.airy.v2plus.util.StringConverter
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.net.CookieManager
import java.net.CookiePolicy


/**
 * Created by Airy on 2019-07-29
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

class V2plusRetrofitService {

    companion object {

        private val persistentCookieStore =
            PersistentCookieStore(getAppContext())

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
        val loggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }


        @JvmField
        val loginClient: OkHttpClient = OkHttpClient.Builder()
            .cookieJar(CustomCookieJar(CookieManager(persistentCookieStore, CookiePolicy.ACCEPT_ALL)))
            .addInterceptor(headersInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()

        @JvmField
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(AppConfigurations.BASE_URL)
            .addConverterFactory(StringConverter())
            .client(loginClient)    // add log
            .build()

        @JvmField
        val v2plusApi: V2plusApi = retrofit.create(V2plusApi::class.java)

        fun getV2plusApi(): V2plusApi = v2plusApi
    }
}