package com.airy.v2plus.ui.notification

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.airy.v2plus.bean.custom.Notification
import com.airy.v2plus.bean.custom.Page
import com.airy.v2plus.repository.NotificationRepository
import com.airy.v2plus.ui.base.BaseViewModel
import com.airy.v2plus.util.UserCenter

class NotificationViewModel : BaseViewModel() {

    val repository: NotificationRepository by lazy { NotificationRepository.getInstance() }

    val showLoginHint = ObservableBoolean(false)

    val notificationPage = MutableLiveData<Page<Notification>>()

    val pagedData = repository.getDataSourceNotificationPages()

    init {
        showLoginHint.set(UserCenter.getUserId() == 0L) // todo better use cookie
    }

    fun refresh() {
//        pagedData.value?.refresh?.invoke()

    }

    fun getNotification(page: Int) {
        launchOnIO({
            val r = repository.getNotificationPages(page)
            notificationPage.postValue(r)
        })
    }

}
