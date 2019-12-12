package com.airy.v2plus.ui.login

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.airy.v2plus.bean.custom.LoginResult
import com.airy.v2plus.repository.UserRepository
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

    private val userRepository = UserRepository.getInstance()

    val loginKey: MutableLiveData<LoginKey> = MutableLiveData()
    val picBitmap: MutableLiveData<Bitmap> = MutableLiveData()
    val loginResult: MutableLiveData<LoginResult> = MutableLiveData()

    init {
        requestLoginKey()
    }

    fun requestLoginKey() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val r = userRepository.getLoginKey()
                withContext(Dispatchers.Main) {
                    loginKey.value = r
                    requestVerifyBitmap(r.once)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun requestVerifyBitmap(key: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val r = userRepository.getVerifyPic(key)
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
                val params = HashMap<String, String>()
                params[loginKey.value!!.userName] = userName
                params[loginKey.value!!.password] = password
                params[loginKey.value!!.verifyCode] = verifyCode
                params["once"] = loginKey.value!!.once
                params["next"] = loginKey.value!!.next
                val r = userRepository.login(params)
                withContext(Dispatchers.Main) {
                    loginResult.value = r
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}