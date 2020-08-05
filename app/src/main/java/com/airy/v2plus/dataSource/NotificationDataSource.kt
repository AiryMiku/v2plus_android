package com.airy.v2plus.dataSource

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.airy.v2plus.network.api.V2plusApi
import com.airy.v2plus.bean.custom.Notification
import com.airy.v2plus.launchOnIOInGlobal
import com.airy.v2plus.repository.util.NetworkState
import com.airy.v2plus.util.UserCenter
import com.airy.v2plus.util.V2exHtmlUtil


/**
 * Created by Airy on 2020-01-15
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

class NotificationDataSource(private val api: V2plusApi): PageKeyedDataSource<Int, Notification>() {

    private val TAG = "NotificationDataSource"

    val liveState = MutableLiveData<NetworkState>()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Notification>
    ) {
        launchOnIOInGlobal(tryBlock = {
            if (UserCenter.getUserId() == 0L) {
                return@launchOnIOInGlobal
            }
            liveState.postValue(NetworkState.LOADING)
            val data = api.getNotificationsResponse(1).let { V2exHtmlUtil.getNotificationPage(it) }
            callback.onResult(data.items, null, let { if (data.isLast()) { null } else { data.current + 1 } })
            liveState.postValue(NetworkState.LOADED)
        }, catchBlock = { e->
            Log.d(TAG, e.message, e)
            liveState.postValue(NetworkState.error(e.message))
        })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Notification>) {
        launchOnIOInGlobal(tryBlock = {
            liveState.postValue(NetworkState.LOADING)
            val data = api.getNotificationsResponse(params.key).let { V2exHtmlUtil.getNotificationPage(it) }
            callback.onResult(data.items, let { if (data.isLast()) { null } else { data.current + 1 } })
            liveState.postValue(NetworkState.LOADED)
        }, catchBlock = { e ->
            Log.d(TAG, e.message, e)
            liveState.postValue(NetworkState.error(e.message))
        })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Notification>) {
        // no used
    }

}