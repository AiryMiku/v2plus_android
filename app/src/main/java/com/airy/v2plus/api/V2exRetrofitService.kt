package com.airy.v2plus.api

import com.airy.v2plus.BuildConfig
import com.airy.v2plus.Config
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Created by Airy on 2019-08-21
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 *
 * official api
 */

class V2exRetrofitService {

    companion object {

        @JvmField
        val client: OkHttpClient = OkHttpClient.Builder().let {
            if (BuildConfig.DEBUG) {
                val loggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
                    this.level = HttpLoggingInterceptor.Level.BODY
                }
                it.addInterceptor(loggingInterceptor)
            }
            it.build()
        }

        @JvmField
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(Config.BASE_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)    // add log
            .build()

        @JvmField
        val v2exApi: V2exApi = retrofit.create(V2exApi::class.java)

        fun getV2exApi(): V2exApi = v2exApi

    }

}