package com.airy.v2plus.ui.login

import androidx.lifecycle.LiveData


/**
 * Created by Airy on 2019-09-02
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

interface LoginViewModelDelegate {

    val loginKey: LiveData<LoginKey>
}