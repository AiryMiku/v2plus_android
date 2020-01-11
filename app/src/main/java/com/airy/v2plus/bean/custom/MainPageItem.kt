package com.airy.v2plus.bean.custom


/**
 * Created by Airy on 2019-07-29
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

data class MainPageItem(
    val avatarUrl: String,
    val title: String,
    val topicId: Long,
    val node: String,
    val topicInfoList: List<String>,
    val commentCount: String
)