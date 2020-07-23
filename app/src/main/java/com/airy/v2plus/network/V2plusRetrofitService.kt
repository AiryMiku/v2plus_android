package com.airy.v2plus.network

import com.airy.v2plus.Config
import com.airy.v2plus.network.api.V2plusApi
import retrofit2.Retrofit


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