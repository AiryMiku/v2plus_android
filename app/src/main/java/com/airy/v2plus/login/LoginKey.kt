package com.airy.v2plus.login


/**
 * Created by Airy on 2019-08-31
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

data class LoginKey(
    val userName: String,
    val password: String,
    val next: String,
    val once: String,
    val shortVerifyCodeUrl: String,
    val verifyCode: String)