package com.airy.v2plus.ui.main

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.airy.v2plus.model.custom.Balance
import com.airy.v2plus.model.custom.MainPageItem
import com.airy.v2plus.model.official.User
import com.airy.v2plus.repository.MainRepository
import com.airy.v2plus.repository.UserRepository
import com.airy.v2plus.base.BaseViewModel
import com.airy.v2plus.util.UserCenter
import com.airy.v2plus.util.V2exHtmlUtil


/**
 * Created by Airy on 2019-07-29
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

class MainViewModel : BaseViewModel() {

    private val userRepository by lazy { UserRepository.getInstance() }
    private val mainRepository by lazy { MainRepository.getInstance() }

    private val mainPageResponse: MutableLiveData<String> = MutableLiveData()

    val user: MutableLiveData<User> = MutableLiveData()

    val mainListItem: LiveData<List<MainPageItem>> = mainPageResponse.switchMap {
        launchOnViewModelScope {
            MutableLiveData<List<MainPageItem>>().apply {
                postValue(V2exHtmlUtil.getMainPageItems(it))
            }
        }
    }

    val pageUserInfo: LiveData<List<String>> = mainPageResponse.switchMap {
        MutableLiveData<List<String>>().apply {
            value = V2exHtmlUtil.getTopUserInfo(it)
        }
    }

    val balance: MediatorLiveData<Balance> = MediatorLiveData()

    val isRedeemed: ObservableBoolean = ObservableBoolean(false)

    init {
        balance.postValue(UserCenter.getLastBalance())
        balance.addSource(mainPageResponse) {
            launchOnIO({
                balance.postValue(parseBalance(it))
            })
        }

        getUserInfoById()
        getMainPageResponse()
    }

    fun getMainPageResponse() {
        launchOnIO({
            mainRepository.fetchMainPageResponse().let { mainPageResponse.postValue(it.value) }
        })
    }

    fun getUserInfoByName() {
        launchOnIO({
            val userName = UserCenter.getUserName()
            if (userName.isEmpty()) {
                return@launchOnIO
            }
            val result = userRepository.getUserInfoByName(userName)
            user.postValue(result)
        })
    }

    private fun getUserInfoById() {
        launchOnIO({
            val userId = UserCenter.getUserId()
            if (userId == 0L) {
                return@launchOnIO
            }
            val result = userRepository.getUserInfoById(userId)
            user.postValue(result)
        })
    }

    private fun parseBalance(response: String): Balance {
        return V2exHtmlUtil.getBalance(response).apply {
            UserCenter.setLastBalance(this)
        }
    }

}