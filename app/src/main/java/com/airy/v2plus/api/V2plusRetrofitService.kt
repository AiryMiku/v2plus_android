package com.airy.v2plus.api

import com.airy.v2plus.AppConfigurations
import com.airy.v2plus.util.StringConverter
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit


/**
 * Created by Airy on 2019-07-29
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

class V2plusRetrofitService {

    companion object {

        @JvmField
        val loggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

        @JvmField
        val loginClient: OkHttpClient = OkHttpClient.Builder()
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