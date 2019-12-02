package com.airy.v2plus.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.airy.v2plus.bean.custom.CellItem
import com.airy.v2plus.bean.official.User
import com.airy.v2plus.repository.MainRepository
import com.airy.v2plus.repository.UserRepository
import com.airy.v2plus.util.JsoupUtil
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

    val mainPageResponse: MutableLiveData<String> = MutableLiveData()

    val user: MutableLiveData<User> = MutableLiveData()

    val mainListItem: MutableLiveData<List<CellItem>> = MutableLiveData()

    fun getMainPageResponse() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val r = MainRepository.getInstance().getMainPageResponse()
                withContext(Dispatchers.Main) {
                    mainPageResponse.value = r
                }
            } catch (e: Exception) {
                Log.e("MainViewModel", e.message, e)
            }
        }
    }

    fun getMainPageList() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = MainRepository.getInstance().getMainPageResponse()
                val dataList= JsoupUtil.getMainPageItems(result)
                withContext(Dispatchers.Main) {
                    mainListItem.value = dataList
                }
            } catch (e: Exception) {
                Log.e("MainViewModel", e.message, e)
            }
        }
    }


    fun getUserInfo(userName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = UserRepository.getInstance().getUserInfo(userName)
                withContext(Dispatchers.Main) {
                    user.value = result
                }
            } catch (e: Exception) {
                Log.e("MainViewModel", e.message, e)
            }
        }
    }

}