package com.airy.v2plus.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.airy.v2plus.bean.custom.PageCell
import com.airy.v2plus.repository.MainPageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {

    val mainPageList: MutableLiveData<List<PageCell>> = MutableLiveData()

    init {
        getMainPageData()
    }

    fun getMainPageData() {
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
