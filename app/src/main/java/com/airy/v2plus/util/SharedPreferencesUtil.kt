package com.airy.v2plus.util

import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.airy.v2plus.App.Companion.getAppContext


/**
 * Created by Airy on 2019-09-16
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

object SharedPreferencesUtil {

    fun getSp(): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(getAppContext())

    fun isAutoRedeem(): Boolean = getSp().getBoolean("auto_redeem", false)
}