package com.airy.v2plus.repository

import com.airy.v2plus.api.V2plusRetrofitService


/**
 * Created by Airy on 2019-07-29
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

class MainRepository {

    companion object {
        @Volatile
        private var instance: MainRepository? = null

        fun getInstance(): MainRepository = instance ?: synchronized(this) {
            instance ?: MainRepository().also { instance = it }
        }
    }

    suspend fun getMainPageResponse(): String {
        return V2plusRetrofitService.getV2plusApi().getMainPageResponse()
    }
}