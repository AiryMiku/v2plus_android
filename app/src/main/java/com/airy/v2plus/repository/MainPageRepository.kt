package com.airy.v2plus.repository

import com.airy.v2plus.api.V2plusRetrofitService
import com.airy.v2plus.bean.custom.PageCell
import com.airy.v2plus.util.JsoupUtil


/**
 * Created by Airy on 2019-07-29
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

class MainPageRepository {

    companion object {
        @Volatile
        private var instance: MainPageRepository? = null

        fun getInstance(): MainPageRepository = instance ?: synchronized(this) {
            instance ?: MainPageRepository().also { instance = it }
        }
    }

    suspend fun getMainPage(): List<PageCell> {
        val response = V2plusRetrofitService.getV2plusApi().getMainPage()
        val dataList= JsoupUtil.getMainPage(response)
        return dataList
    }
}