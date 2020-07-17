package com.airy.v2plus.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.airy.v2plus.api.V2plusRetrofitService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


/**
 * Created by Airy on 2019-07-29
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

class MainRepository: Repository {

    companion object {
        @Volatile
        private var instance: MainRepository? = null

        fun getInstance(): MainRepository = instance ?: synchronized(this) {
            instance ?: MainRepository().also { instance = it }
        }
    }

    suspend fun fetchMainPageResponse(): LiveData<String> = withContext(Dispatchers.IO) {
        val liveData = MutableLiveData<String>()
        val responseString = V2plusRetrofitService.getV2plusApi().getMainPageResponse()

        liveData.apply { postValue(responseString) }
    }


}