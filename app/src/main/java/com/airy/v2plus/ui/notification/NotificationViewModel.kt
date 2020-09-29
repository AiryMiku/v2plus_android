package com.airy.v2plus.ui.notification

import androidx.databinding.ObservableBoolean
import com.airy.v2plus.network.RequestHelper
import com.airy.v2plus.repository.NotificationRepository
import com.airy.v2plus.base.BaseViewModel

class NotificationViewModel : BaseViewModel() {

    val repository: NotificationRepository by lazy { NotificationRepository.getInstance() }

    val showLoginHint = ObservableBoolean(false)

    private val listing = repository.getNotificationListing()

    val notifications = listing.pagedList

    val networkState = listing.networkState

    init {
        refresh()
    }

    fun refresh() {
        if (RequestHelper.isCookieExpired()) {
            showLoginHint.set(true)
        } else {
            showLoginHint.set(false)
            listing.refresh.invoke()
        }
    }

}
