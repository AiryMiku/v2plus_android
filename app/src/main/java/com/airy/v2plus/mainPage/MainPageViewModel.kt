package com.airy.v2plus.mainPage

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.airy.v2plus.bean.custom.PageCell
import com.airy.v2plus.repository.MainPageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * Created by Airy on 2019-07-29
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

class MainPageViewModel :ViewModel() {

    val mainPageList: MutableLiveData<List<PageCell>> = MutableLiveData()

    init {
//        getMainPageData()
    }

    private fun getMainPageData() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = MainPageRepository.getInstance().getMainPage()
                withContext(Dispatchers.Main) {
                    mainPageList.value = result
                }
            } catch (e: Exception) {
                Log.e("MainPageViewModel", e.message, e)
            }
        }
    }
}