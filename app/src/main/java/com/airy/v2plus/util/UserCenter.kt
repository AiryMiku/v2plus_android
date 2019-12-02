package com.airy.v2plus.util

import com.airy.v2plus.util.SharedPreferencesUtil.getSp


/**
 * Created by Airy on 2019-11-25
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

class UserCenter {

    companion object {
        const val KEY_USER_COOKIE_EXPIRED = "KEY_USER_COOKIE_EXPIRED"

        fun isUserCookieExpired(): Boolean {
            return getSp().getBoolean(KEY_USER_COOKIE_EXPIRED, true)
        }

        fun setUserCookieExpires(boolean: Boolean) {
            getSp().edit().putBoolean(KEY_USER_COOKIE_EXPIRED, boolean).apply()
        }
    }

}