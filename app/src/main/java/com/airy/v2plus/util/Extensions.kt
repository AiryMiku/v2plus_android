package com.airy.v2plus.util

import com.airy.v2plus.util.SharedPreferencesUtil.getSp
/**
 * Created by Airy on 2019-09-20
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

//@SuppressLint("WrongConstant")
//fun AppCompatActivity.updateForTheme(theme: Resources.Theme) = when (theme) {
//    Resources.Theme.DARK -> delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
//    Resources.Theme.LIGHT -> delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
//    Resources.Theme.SYSTEM -> delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
//    Resources.Theme.BATTERY_SAVER -> delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
//}
private const val IS_DARK_MODE = "IS_DARK_MODE"

fun getDarkModeStorage(): Boolean {
   return getSp().getBoolean(IS_DARK_MODE, false)
}

fun setDarkModeStorage(b: Boolean) {
    getSp().edit().putBoolean(IS_DARK_MODE, b).apply()
}