package com.airy.v2plus.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.airy.v2plus.network.V2exRetrofitService
import com.airy.v2plus.network.V2plusRetrofitService
import com.airy.v2plus.bean.custom.LoginResult
import com.airy.v2plus.bean.official.User
import com.airy.v2plus.ui.login.LoginKey
import com.airy.v2plus.util.V2exHtmlUtil


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

    suspend fun getLoginKey(): LoginKey {
        val response = V2plusRetrofitService.getV2plusApi().getLoginResponse()
        return V2exHtmlUtil.getLoginValue(response)
    }

    // 真是绝了
    suspend fun getVerifyPic(key: String): Bitmap {
        val response = V2plusRetrofitService.getV2plusApi().getVerifyPic(key).byteStream()
        return BitmapFactory.decodeStream(response)
    }

    suspend fun login(params: HashMap<String, String>): LoginResult {
        val response = V2plusRetrofitService.getV2plusApi().postLogin(params)
        return V2exHtmlUtil.getLoginResult(response)
    }

    suspend fun getUserInfoByName(userName: String): User {
        return V2exRetrofitService.getV2exApi().getUserByName(userName)
    }

    suspend fun getUserInfoById(userId: Long): User {
        return V2exRetrofitService.getV2exApi().getUserById(userId)
    }

    suspend fun getDailyMissionResponse(): String {
        return V2plusRetrofitService.getV2plusApi().getDailyMissionResponse()
    }

    suspend fun getDailyMissionRedeemResponse(once: String): String {
        return V2plusRetrofitService.getV2plusApi().getDailyMissionResponse(once)
    }
}