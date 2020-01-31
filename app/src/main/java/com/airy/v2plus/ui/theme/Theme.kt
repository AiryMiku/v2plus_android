package com.airy.v2plus.ui.theme


/**
 * Created by Airy on 2020-01-20
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

enum class Theme(val storageKey: String) {
    LIGHT("light"),
    DARK("dark"),
    SYSTEM("system"),
    BATTERY_SAVER("battery_saver")
}

fun themeFromStorageKey(storageKey: String): Theme {
    return Theme.values().first { it.storageKey == storageKey }
}