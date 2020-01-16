package com.airy.v2plus.repository

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.airy.v2plus.api.V2plusRetrofitService
import com.airy.v2plus.bean.custom.Notification
import com.airy.v2plus.bean.custom.Page
import com.airy.v2plus.dataSource.NotificationDataSourceFactory
import com.airy.v2plus.util.JsoupUtil


/**
 * Created by Airy on 2020-01-13
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

class NotificationRepository {
    companion object {
        @Volatile
        private var instance: NotificationRepository? = null

        fun getInstance(): NotificationRepository = instance ?: synchronized(this) {
            instance ?: NotificationRepository().also { instance = it }
        }
    }

    suspend fun getNotificationPages(page: Int): Page<Notification> {
        val r = V2plusRetrofitService.getV2plusApi().getNotificationsResponse(page)
        return JsoupUtil.getNotificationPage(r)
    }

    @MainThread
    fun getDataSourceNotificationPages() : LiveData<PagedList<Notification>> {
        val factory = NotificationDataSourceFactory()
        return factory.toLiveData(10)
    }
}