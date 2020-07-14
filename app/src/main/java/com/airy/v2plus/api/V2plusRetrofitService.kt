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
import java.util.concurrent.TimeUnit


/**
 * Created by Airy on 2019-07-29
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

class V2plusRetrofitService {

    companion object {

        private const val CONNECT_TIME_OUT = 30L //连接超时时间
        private const val READ_TIME_OUT = 30L

        
        private const val TAG = "V2plusRetrofitService"

        private val persistentCookieStore = PersistentCookieStore(getAppContext())

        @JvmField
        val headersInterceptor: Interceptor = object :Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val original = chain.request()

                val request = original.newBuilder().apply {
                    header("User-Agent", Config.USER_AGENT)
                }.build()

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
            cookieJar(CookieJarImpl(persistentCookieStore))
            addInterceptor(headersInterceptor)
            addInterceptor(errorInterceptor)
            retryOnConnectionFailure(true)
            followRedirects(false)
        }.build()



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