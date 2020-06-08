package com.airy.v2plus.dataSource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.airy.v2plus.api.V2plusRetrofitService
import com.airy.v2plus.bean.custom.Notification


/**
 * Created by Airy on 2020-01-16
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

class NotificationDataSourceFactory: DataSource.Factory<Int, Notification>() {

    val sourceLiveData = MutableLiveData<NotificationDataSource>()

    override fun create(): DataSource<Int, Notification> {
        val source = NotificationDataSource(V2plusRetrofitService.getV2plusApi())
        sourceLiveData.postValue(source)
        return source
    }
}