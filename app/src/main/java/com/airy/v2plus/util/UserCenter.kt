package com.airy.v2plus.util

import com.airy.v2plus.bean.custom.Balance
import com.airy.v2plus.util.SharedPreferencesUtil.getSp


/**
 * Created by Airy on 2019-11-25
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

class UserCenter {

    companion object {
        private const val KEY_USER_COOKIE_EXPIRED = "KEY_USER_COOKIE_EXPIRED"
        private const val KEY_USER_ID = "KEY_USER_ID"
        private const val KEY_USER_NAME = "KEY_USER_NAME"
        private const val KEY_LAST_BALANCE = "KEY_LAST_BALANCE"

        fun isUserCookieExpired(): Boolean {
            return getSp().getBoolean(KEY_USER_COOKIE_EXPIRED, true)
        }

        fun setUserCookieExpires(boolean: Boolean) {
            getSp().edit().putBoolean(KEY_USER_COOKIE_EXPIRED, boolean).apply()
        }

        fun getUserId(): Long = getSp().getLong(KEY_USER_ID, 0L)

        fun setUserId(userId: Long) {
            getSp().edit().putLong(KEY_USER_ID, userId).apply()
        }

        fun getUserName(): String = getSp().getString(KEY_USER_NAME, "") ?: ""

        fun setUserName(name: String) {
            getSp().edit().putString(KEY_USER_NAME, name).apply()
        }

        fun getLastBalance(): Balance {
            val b = getSp().getString(KEY_LAST_BALANCE, "")
            return if (b == null || b.isBlank()) {
                Balance()
            } else {
                val list = b.split("-")
                Balance(list[0], list[1], list[2])
            }
        }

        fun setLastBalance(balance: Balance) {
            getSp().edit().putString(KEY_LAST_BALANCE, "${balance.gold}-${balance.silver}-${balance.bronze}").apply()
        }
    }

}