package com.airy.v2plus.util

import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.airy.v2plus.V2plusApp.Companion.getAppContext


/**
 * Created by Airy on 2019-09-16
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

object SharedPreferencesUtil {

    fun getSp(): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(getAppContext())

}