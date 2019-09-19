package com.airy.v2plus.api.cookie

import com.airy.v2plus.util.SharedPreferencesUtil.getSp


/**
 * Created by Airy on 2019-09-16
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

class TokenCache {

    companion object {
        private const val KEY = "TOKEN_CACHE"

        fun saveToken(token: String): Any = getSp().edit().putString(KEY, token).apply()

        fun getToken(): String? = getSp().getString(KEY, "")

        fun clearToken(): Any = getSp().edit().putString(KEY, "").apply()
    }

}