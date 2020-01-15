package com.airy.v2plus.repository.dataSource

import androidx.paging.PageKeyedDataSource
import com.airy.v2plus.bean.custom.Notification
import com.airy.v2plus.repository.NotificationRepository


/**
 * Created by Airy on 2020-01-15
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

class NotificationDataSource(private val repository: NotificationRepository): PageKeyedDataSource<Long, Notification>() {

    override fun loadInitial(
        params: LoadInitialParams<Long>,
        callback: LoadInitialCallback<Long, Notification>
    ) {

    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Long, Notification>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Long, Notification>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}