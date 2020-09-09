package com.airy.v2plus.db

import androidx.room.Dao
import androidx.room.Query
import com.airy.v2plus.model.custom.Notification

/**
 * Created by Airy on 2020/9/9
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */
@Dao
interface NotificationDao {

    @Query("SELECT * from notifications")
    suspend fun getAllNotifications(): List<Notification>
}