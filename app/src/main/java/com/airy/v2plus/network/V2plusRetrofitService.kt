package com.airy.v2plus.network

import com.airy.v2plus.App
import com.airy.v2plus.App.Companion.getAppContext
import com.airy.v2plus.BuildConfig
import com.airy.v2plus.Config
import com.airy.v2plus.network.api.V2plusApi
import com.airy.v2plus.showToastLong
import com.airy.v2plus.util.StringConverter
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
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
        private const val TAG = "V2plusRetrofitService"

        @JvmField
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(Config.BASE_URL)
            .addConverterFactory(StringConverter())
            .client(RequestHelper.client)
            .build()

        @JvmField
        val v2plusApi: V2plusApi = retrofit.create(V2plusApi::class.java)

        fun getV2plusApi(): V2plusApi = v2plusApi
    }
}