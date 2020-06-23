package com.airy.v2plus.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.airy.v2plus.bean.custom.Balance
import com.airy.v2plus.bean.custom.MainPageItem
import com.airy.v2plus.bean.official.User
import com.airy.v2plus.component.Event
import com.airy.v2plus.repository.MainRepository
import com.airy.v2plus.repository.UserRepository
import com.airy.v2plus.ui.base.BaseViewModel
import com.airy.v2plus.util.V2exHtmlUtil
import com.airy.v2plus.util.UserCenter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


/**
 * Created by Airy on 2019-07-29
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

class MainViewModel :BaseViewModel() {

    private val userRepository by lazy { UserRepository.getInstance() }
    private val mainRepository by lazy { MainRepository.getInstance() }

    private val mainPageResponse: MutableLiveData<String> = MutableLiveData()

    val user: MutableLiveData<User> = MutableLiveData()

    val mainListItem: MutableLiveData<List<MainPageItem>> = MutableLiveData()

    val balance: MutableLiveData<Balance> = MutableLiveData()

//    val isRedeem: MutableLiveData<Boolean> = MutableLiveData()

    val redeemMessages: MutableLiveData<Event<List<String>>> = MutableLiveData()


//    val pageUserInfo: MutableLiveData<List<String>> = MutableLiveData()

    init {
        getMainPageResponse()
    }

    fun getMainPageResponse() {
        launchOnIO({
            val r = mainRepository.getMainPageResponse()
            mainPageResponse.postValue(r)
            val dataList = V2exHtmlUtil.getMainPageItems(r)
            mainListItem.postValue(dataList)
            getBalance(r)
        }, {
            t -> Log.e("MainViewModel", t.message, t)
        })
    }


    fun getUserInfoByName() {
        launchOnIO({
            val userName = UserCenter.getUserName()
            if (userName.isEmpty()) {
                throw NullPointerException("User name has not been saved!")
            }
            val result = userRepository.getUserInfoByName(userName)
            user.postValue(result)
        }, {
            t -> Log.e("MainViewModel", t.message, t)
        })
    }

    fun getUserInfoById() {
        launchOnIO({
            val userId = UserCenter.getUserId()
            if (userId == 0L) {
                throw NullPointerException("User id has not been saved!")
            }
            val result = userRepository.getUserInfoById(userId)
            withContext(Dispatchers.Main) {
                user.value = result
            }
        }, {
            t -> Log.e("MainViewModel", t.message, t)
        })
    }

    fun getDailyMissionRedeem() {
        launchOnIO({
            val redeemResponse: String
            val response = userRepository.getDailyMissionResponse()
            val param = V2exHtmlUtil.getDailyMissionParam(response)
            redeemResponse = if (param != "") {
                userRepository.getDailyMissionRedeemResponse(param)
            } else {
                response
            }
            val redeemMessages = V2exHtmlUtil.getDailyMissionRedeemResult(redeemResponse)
            getBalance(redeemResponse)
            this@MainViewModel.redeemMessages.postValue(Event(redeemMessages))
        }, {
                t -> Log.e("MainViewModel", t.message, t)
        })
    }

    fun getBalance(response: String) {
        val result = V2exHtmlUtil.getBalance(response)
        UserCenter.setLastBalance(result)
        balance.postValue(result)
    }

}