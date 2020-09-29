package com.airy.v2plus.ui.login

import androidx.lifecycle.MutableLiveData
import com.airy.v2plus.model.custom.LoginResult
import com.airy.v2plus.repository.UserRepository
import com.airy.v2plus.base.BaseViewModel


/**
 * Created by Airy on 2019-08-31
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

class LoginViewModel : BaseViewModel() {

    private val userRepository = UserRepository.getInstance()

    val loginKey: MutableLiveData<LoginKey> = MutableLiveData()
    val loginResult: MutableLiveData<LoginResult> = MutableLiveData()

    init {
        requestLoginKey()
    }

    fun requestLoginKey() {
        launchOnIO({
            val r = userRepository.getLoginKey()
            loginKey.postValue(r)
        })
    }

    fun doLogin(userName: String, password: String, verifyCode: String) {
        launchOnIO({
            val params = HashMap<String, String>()
            params[loginKey.value!!.userName] = userName
            params[loginKey.value!!.password] = password
            params[loginKey.value!!.verifyCode] = verifyCode
            params["once"] = loginKey.value!!.once
            params["next"] = loginKey.value!!.next
            val r = userRepository.login(params)
            loginResult.postValue(r)
        })
    }
}