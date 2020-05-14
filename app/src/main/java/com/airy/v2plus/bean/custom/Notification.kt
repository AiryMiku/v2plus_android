package com.airy.v2plus.bean.custom


/**
 * Created by Airy on 2020-01-11
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

data class Notification(
    val avatarUrl: String,
    val userName: String,
    val title: String,
    val topicId: Long,
    val replyNo: Long,
    val time: String,
    val payload: String
)