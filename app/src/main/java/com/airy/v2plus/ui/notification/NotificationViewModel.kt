package com.airy.v2plus.ui.notification

import androidx.lifecycle.MutableLiveData
import com.airy.v2plus.bean.custom.Notification
import com.airy.v2plus.bean.custom.Page
import com.airy.v2plus.repository.NotificationRepository
import com.airy.v2plus.ui.base.BaseViewModel

class NotificationViewModel : BaseViewModel() {

    val repository: NotificationRepository by lazy { NotificationRepository.getInstance() }

    val notificationPage = MutableLiveData<Page<Notification>>()

    val pagedData = repository.getDataSourceNotificationPages()

    fun getNotification(page: Int) {
        launchOnIO({
            val r = repository.getNotificationPages(page)
            notificationPage.postValue(r)
        })
    }

}
