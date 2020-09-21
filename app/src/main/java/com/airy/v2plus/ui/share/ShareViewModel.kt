package com.airy.v2plus.ui.share

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.airy.v2plus.network.RequestHelper
import com.airy.v2plus.ui.theme.Theme
import com.airy.v2plus.util.getDarkModeStorage


/**
 * Created by Airy on 2020-01-20
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

class ShareViewModel: ViewModel() {

    val theme: MutableLiveData<Theme> by lazy {
        if (getDarkModeStorage()) {
            MutableLiveData(Theme.DARK)
        } else {
            MutableLiveData(Theme.LIGHT)
        }
    }

    // todo control ui
    val isUserCookieExpired: MutableLiveData<Boolean> by lazy {
        MutableLiveData(RequestHelper.isCookieExpired())
    }

    fun refreshCookie() {
        isUserCookieExpired.postValue(RequestHelper.isCookieExpired())
    }

}