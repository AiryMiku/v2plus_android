package com.airy.v2plus.model.custom

import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * Created by Airy on 2020-01-11
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */
@Entity(
    tableName = "notifications"
)
data class Notification(
    val avatarUrl: String,
    val userName: String,
    val title: String,
    val topicId: Long,
    val replyNo: Long,
    val time: String,
    val payload: String,
    val isReply: Boolean = true,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)