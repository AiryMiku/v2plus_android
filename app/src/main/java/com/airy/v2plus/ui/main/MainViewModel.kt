package com.airy.v2plus.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.airy.v2plus.bean.custom.Balance
import com.airy.v2plus.bean.custom.CellItem
import com.airy.v2plus.bean.official.User
import com.airy.v2plus.repository.MainRepository
import com.airy.v2plus.repository.UserRepository
import com.airy.v2plus.util.JsoupUtil
import com.airy.v2plus.util.UserCenter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * Created by Airy on 2019-07-29
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

class MainViewModel :ViewModel() {

    private val userRepository = UserRepository.getInstance()

    val mainPageResponse: MutableLiveData<String> = MutableLiveData()

    val user: MutableLiveData<User> = MutableLiveData()

    val mainListItem: MutableLiveData<List<CellItem>> = MutableLiveData()

    val balance: MutableLiveData<Balance> = MutableLiveData()

    val isRedeem: MutableLiveData<Boolean> = MutableLiveData()

    val redeemMessages: MutableLiveData<List<String>> = MutableLiveData()

//    val pageUserInfo: MutableLiveData<List<String>> = MutableLiveData()

    fun getMainPageResponse() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val r = MainRepository.getInstance().getMainPageResponse()
                getMainPageList(r)
                val br = JsoupUtil.getBalance(r)
                withContext(Dispatchers.Main) {
                    mainPageResponse.value = r
                    balance.value = br
                }
            } catch (e: Exception) {
                Log.e("MainViewModel", e.message, e)
            }
        }
    }

    private fun getMainPageList(response: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val dataList= JsoupUtil.getMainPageItems(response)
                withContext(Dispatchers.Main) {
                    mainListItem.value = dataList
                }
            } catch (e: Exception) {
                Log.e("MainViewModel", e.message, e)
            }
        }
    }


    fun getUserInfoByName() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userName = UserCenter.getUserName()
                if (userName.isEmpty()) {
                    throw Exception("User name has not been saved!!!")
                }
                val result = userRepository.getUserInfoByName(userName)
                withContext(Dispatchers.Main) {
                    user.value = result
                }
            } catch (e: Exception) {
                Log.e("MainViewModel", e.message, e)
            }
        }
    }

    fun getUserInfoById() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userId = UserCenter.getUserId()
                if (userId == 0L) {
                    return@launch
                }
                val result = userRepository.getUserInfoById(userId)
                withContext(Dispatchers.Main) {
                    user.value = result
                }
            } catch (e: Exception) {
                Log.e("MainViewModel", e.message, e)
            }
        }
    }

    fun getDailyMissionRedeem() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val redeemResponse: String
                val response = userRepository.getDailyMissionResponse()
                val param = JsoupUtil.getDailyMissionParam(response)
                redeemResponse = if (param != "") {
                    userRepository.getDailyMissionRedeemResponse(param)
                } else {
                    response
                }
                val redeemMessages = JsoupUtil.getDailyMissionRedeemResult(redeemResponse)
                getBalance(redeemResponse)
                withContext(Dispatchers.Main) {
                    this@MainViewModel.redeemMessages.value = redeemMessages
                }
            } catch (e: Exception) {
                Log.e("MainViewModel", e.message, e)
            }
        }
    }

    fun getBalance(response: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = JsoupUtil.getBalance(response)
                withContext(Dispatchers.Main) {
                    balance.value = result
                }
            } catch (e: Exception) {
                Log.e("MainViewModel", e.message, e)
            }
        }
    }

}