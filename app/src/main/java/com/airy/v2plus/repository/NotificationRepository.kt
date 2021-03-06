package com.airy.v2plus.repository

import androidx.annotation.MainThread
import androidx.lifecycle.switchMap
import androidx.paging.Config
import androidx.paging.toLiveData
import com.airy.v2plus.network.V2plusRetrofitService
import com.airy.v2plus.model.custom.Notification
import com.airy.v2plus.model.custom.Page
import com.airy.v2plus.dataSource.NotificationDataSourceFactory
import com.airy.v2plus.repository.util.Listing
import com.airy.v2plus.util.V2exHtmlUtil


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

    @MainThread
    fun getNotificationListing(): Listing<Notification> {
        val sourceFactory = NotificationDataSourceFactory(V2plusRetrofitService.v2plusApi)
        val livePagedList = sourceFactory.toLiveData(Config(
            pageSize = 10,
            prefetchDistance = 1
        ))

        return Listing(
            pagedList = livePagedList,
            networkState = sourceFactory.sourceLiveData.switchMap {
                it.liveState
            },
            refresh = {
                sourceFactory.sourceLiveData.value?.invalidate()
            }
        )
    }
}