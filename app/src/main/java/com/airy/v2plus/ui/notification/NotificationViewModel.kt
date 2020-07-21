package com.airy.v2plus.ui.notification

import androidx.databinding.ObservableBoolean
import com.airy.v2plus.repository.NotificationRepository
import com.airy.v2plus.ui.base.BaseViewModel
import com.airy.v2plus.util.UserCenter

class NotificationViewModel : BaseViewModel() {

    val repository: NotificationRepository by lazy { NotificationRepository.getInstance() }

    val showLoginHint = ObservableBoolean(false)

    private val listing = repository.getNotificationListing()

    val notifications = listing.pagedList

    val networkState = listing.networkState

    init {
        if (UserCenter.getUserId() != 0L) {     // todo better use cookie
            showLoginHint.set(true)
            refresh()
        }
    }

    fun refresh() {
        listing.refresh.invoke()
    }

//    val notificationPage = MutableLiveData<Page<Notification>>()
//    fun getNotification(page: Int) {
//        launchOnIO({
//            val r = repository.getNotificationPages(page)
//            notificationPage.postValue(r)
//        })
//    }

}
