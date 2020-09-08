package com.airy.v2plus.dataSource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.airy.v2plus.network.api.V2plusApi
import com.airy.v2plus.model.custom.Notification


/**
 * Created by Airy on 2020-01-16
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

class NotificationDataSourceFactory(val api: V2plusApi): DataSource.Factory<Int, Notification>() {

    val sourceLiveData = MutableLiveData<NotificationDataSource>()

    override fun create(): DataSource<Int, Notification> {
        val source = NotificationDataSource(api)
        sourceLiveData.postValue(source)
        return source
    }
}