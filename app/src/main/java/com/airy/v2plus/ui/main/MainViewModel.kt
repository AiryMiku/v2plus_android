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
import com.airy.v2plus.coroutineLiveData
import com.airy.v2plus.util.UserCenter
import com.airy.v2plus.util.V2exHtmlUtil
import kotlinx.coroutines.channels.Channel


/**
 * Created by Airy on 2019-07-29
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

interface MainViewModelDelegate {
    val userRepository: UserRepository
    val mainRepository: MainRepository

    val mainPageResponse: MutableLiveData<String>

    val user: MutableLiveData<User>

    val mainListItem: LiveData<List<MainPageItem>>

    val pageUserInfo: LiveData<List<String>>

    val balance: MediatorLiveData<Balance>

    val isRedeemed: ObservableBoolean
}

class MainViewModel : BaseViewModel() {

    /**
     * internal var
     */
    private val mainPageChannel = Channel<String>(1)
    private var _mainPageResponse = mainPageChannel.coroutineLiveData

    private val userRepository by lazy { UserRepository.getInstance() }
    private val mainRepository by lazy { MainRepository.getInstance() }

    val user: MutableLiveData<User> = MutableLiveData()

    val mainListItem: LiveData<List<MainPageItem>> = _mainPageResponse.switchMap {
        launchOnViewModelScope {
            MutableLiveData<List<MainPageItem>>().apply {
                postValue(V2exHtmlUtil.getMainPageItems(it))
            }
        }
    }

    val pageUserInfo: LiveData<List<String>> = _mainPageResponse.switchMap {
        MutableLiveData<List<String>>().apply {
            value = V2exHtmlUtil.getTopUserInfo(it)
        }
    }

    val balance: MediatorLiveData<Balance> = MediatorLiveData()

    val isRedeemed: ObservableBoolean = ObservableBoolean(false)

    init {
        balance.postValue(UserCenter.getLastBalance())
        balance.addSource(_mainPageResponse) {
            launchOnIO({
                balance.postValue(parseBalance(it))
            })
        }

        getUserInfoById()
        getMainPageResponse()
    }

    fun getMainPageResponse() {
        launchOnIO({
            val result = mainRepository.fetchMainPageResponse()
            mainPageChannel.send(result)
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