package com.airy.v2plus.api

import retrofit2.http.*




/**
 * Created by Airy on 2019-08-21
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

interface V2plusApi {

    // 获取登陆页面信息
    @GET("signin")
    suspend fun login(): String

    // 传入登陆信息进行登陆
    @FormUrlEncoded
    @Headers("Referer: https://www.v2ex.com/signin")
    @POST("signin")
    suspend fun postLogin(@FieldMap hashMap: HashMap<String, String>): String


    @GET("/")
    suspend fun getMainPage(): String
}