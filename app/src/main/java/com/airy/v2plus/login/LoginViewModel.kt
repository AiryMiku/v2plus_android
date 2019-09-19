package com.airy.v2plus.login

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.airy.v2plus.repository.LoginRepository
import com.airy.v2plus.util.isBlankOrEmpty
import com.airy.v2plus.util.showLong
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * Created by Airy on 2019-08-31
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

class LoginViewModel: ViewModel() {

    private val repository = LoginRepository.getInstance()

    val loginKey: MutableLiveData<LoginKey> = MutableLiveData()
    val picBitmap: MutableLiveData<Bitmap> = MutableLiveData()
    val loginResult: MutableLiveData<String> = MutableLiveData()

    init {
        requestLoginKey()
    }

    fun requestLoginKey() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val r = repository.getLoginKey()
                withContext(Dispatchers.Main) {
                    loginKey.value = r
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun requestVerifyBitmap(key: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val r = repository.getVerifyPic(key)
                withContext(Dispatchers.Main) {
                    picBitmap.value = r
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun doLogin(userName: String, password: String, verifyCode: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                if (isBlankOrEmpty(userName, password, verifyCode)) {
                    showLong("Args must not be blank or empty")
                    return@launch
                }
                if (loginKey.value == null) {
                    showLong("Login key is null")
                    return@launch
                } else {
                    val params = HashMap<String, String>()
                    params[loginKey.value!!.userName] = userName
                    params[loginKey.value!!.password] = password
                    params[loginKey.value!!.verifyCode] = verifyCode
                    params["once"] = loginKey.value!!.once
                    params["next"] = loginKey.value!!.next
                    val r = repository.login(params)
                    withContext(Dispatchers.Main) {
                        loginResult.value = r
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}