package com.airy.v2plus.dataSource

import androidx.paging.PageKeyedDataSource
import com.airy.v2plus.api.V2plusApi
import com.airy.v2plus.bean.custom.Notification
import com.airy.v2plus.util.JsoupUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * Created by Airy on 2020-01-15
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

class NotificationDataSource(private val api: V2plusApi): PageKeyedDataSource<Int, Notification>() {

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Notification>
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val data = api.getNotificationsResponse(1).let { JsoupUtil.getNotificationPage(it) }
            callback.onResult(data.items, null, let { if (data.isLast()) { null } else { data.current + 1 } })
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Notification>) {
        CoroutineScope(Dispatchers.IO).launch {
            val data = api.getNotificationsResponse(params.key).let { JsoupUtil.getNotificationPage(it) }
            callback.onResult(data.items, let { if (data.isLast()) { null } else { data.current + 1 } })
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Notification>) {
        // no used
    }

}