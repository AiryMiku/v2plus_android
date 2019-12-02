package com.airy.v2plus.repository

import com.airy.v2plus.api.V2exRetrofitService
import com.airy.v2plus.bean.official.User


/**
 * Created by Airy on 2019-11-13
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

class UserRepository {

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(): UserRepository = instance ?: synchronized(this) {
            instance ?: UserRepository().also { instance = it }
        }
    }

    suspend fun getUserInfo(userName: String): User {
        return V2exRetrofitService.getV2exApi().getUserInfo(userName)
    }
}