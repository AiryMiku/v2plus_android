package com.airy.v2plus.api

import okhttp3.ResponseBody
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

    // 获取验证码图片
    @GET("https://www.v2ex.com/_captcha")
    @Headers("referer: https://www.v2ex.com/signin",
        "accept: image/webp,image/apng,image/*,*/*;q=0.8",
        "accept-encoding: gzip, deflate, br",
        "accept-language: en-US,en;q=0.9,zh-TW;q=0.8,zh-HK;q=0.7,zh-CN;q=0.6,zh;q=0.5,ja-JP;q=0.4,ja;q=0.3",
        "dnt: 1",
        "sec-fetch-mode: no-cors",
        "sec-fetch-site: same-origin")
    suspend fun getVerifyPic(@Query("once") key: String): ResponseBody

    // 传入登陆信息进行登陆
    @FormUrlEncoded
    @Headers("Referer: https://www.v2ex.com/signin")
    @POST("signin")
    suspend fun postLogin(@FieldMap hashMap: HashMap<String, String>): String


    @GET("/")
    suspend fun getMainPage(): String
}