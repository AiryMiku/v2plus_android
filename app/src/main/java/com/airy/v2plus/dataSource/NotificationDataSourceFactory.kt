package com.airy.v2plus.dataSource

import androidx.paging.DataSource
import com.airy.v2plus.api.V2plusRetrofitService
import com.airy.v2plus.bean.custom.Notification


/**
 * Created by Airy on 2020-01-16
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

class NotificationDataSourceFactory: DataSource.Factory<Int, Notification>() {
    override fun create(): DataSource<Int, Notification> {
        return NotificationDataSource(V2plusRetrofitService.getV2plusApi())
    }
}