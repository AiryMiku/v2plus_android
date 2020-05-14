package com.airy.v2plus.dataSource

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.airy.v2plus.api.V2plusApi
import com.airy.v2plus.bean.Status
import com.airy.v2plus.bean.custom.Notification
import com.airy.v2plus.util.V2exHtmlUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * Created by Airy on 2020-01-15
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

class NotificationDataSource(private val api: V2plusApi): PageKeyedDataSource<Int, Notification>() {

    private val TAG = "NotificationDataSource"

    val status = MutableLiveData<Status>()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Notification>
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                status.postValue(Status.LOADING)
                val data = api.getNotificationsResponse(1).let { V2exHtmlUtil.getNotificationPage(it) }
                callback.onResult(data.items, null, let { if (data.isLast()) { null } else { data.current + 1 } })
                status.postValue(Status.FINISH)
            } catch (e: Exception) {
                Log.d(TAG, e.message, e)
                status.postValue(Status.ERROR)
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Notification>) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                status.postValue(Status.LOADING)
                val data = api.getNotificationsResponse(params.key).let { V2exHtmlUtil.getNotificationPage(it) }
                callback.onResult(data.items, let { if (data.isLast()) { null } else { data.current + 1 } })
                status.postValue(Status.FINISH)
            } catch (e: Exception) {
                Log.d(TAG, e.message, e)
                status.postValue(Status.ERROR)
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Notification>) {
        // no used
    }

}