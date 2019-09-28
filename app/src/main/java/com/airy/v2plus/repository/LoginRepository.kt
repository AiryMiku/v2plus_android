package com.airy.v2plus.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.airy.v2plus.api.V2plusRetrofitService
import com.airy.v2plus.bean.custom.LoginResult
import com.airy.v2plus.login.LoginKey
import com.airy.v2plus.util.JsoupUtil


/**
 * Created by Airy on 2019-09-11
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

class LoginRepository {

    companion object {
        @Volatile
        private var instance: LoginRepository? = null

        fun getInstance(): LoginRepository = instance ?: synchronized(this) {
            instance ?: LoginRepository().also { instance = it }
        }
    }

    suspend fun getLoginKey(): LoginKey {
        val response = V2plusRetrofitService.getV2plusApi().login()
        return JsoupUtil.getLoginValue(response)
    }

    suspend fun getVerifyPic(key: String): Bitmap {
        val response = V2plusRetrofitService.getV2plusApi().getVerifyPic(key).byteStream()
        return BitmapFactory.decodeStream(response)
    }

    suspend fun login(params: HashMap<String, String>): LoginResult {
        val response = V2plusRetrofitService.getV2plusApi().postLogin(params)
        return JsoupUtil.getLoginResult(response)
    }
}