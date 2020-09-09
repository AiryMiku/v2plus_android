package com.airy.v2plus.network

import com.airy.v2plus.BuildConfig
import com.airy.v2plus.Config
import com.airy.v2plus.network.api.V2exApi
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

object V2exRetrofitService {

    @JvmField
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(Config.BASE_API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(RequestHelper.client)
        .build()

    @JvmField
    val v2exApi: V2exApi = retrofit.create(V2exApi::class.java)

    fun getV2exApi(): V2exApi = v2exApi

}