package com.airy.v2plus.model.custom


/**
 * Created by Airy on 2019-09-19
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

data class LoginResult(
    val problems: List<String>,
    val userName: String,
    val avatarUrl: String
)