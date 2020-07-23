package com.airy.v2plus.util

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs


/**
 * Created by Airy on 2020-01-10
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

object DateUtil {

    fun formatTime(timeStamp: Long): String {

        val targetDate = Date(timeStamp * 1000)

        val calendar: Calendar = Calendar.getInstance()
        calendar.add(Calendar.HOUR_OF_DAY, -12)

        if (calendar.get(Calendar.HOUR_OF_DAY) >= 12 && calendar.get(Calendar.AM_PM) == 1) {
            calendar.add(Calendar.HOUR_OF_DAY, -12)
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        val nowTimeLong: Long = calendar.timeInMillis
        val currentTimeLong: Long = targetDate.time
        val result = abs(nowTimeLong - currentTimeLong)

        return if (result < 60000) { // 一分钟内
            val seconds = result / 1000
            if (seconds == 0L) {
                "刚刚"
            } else {
                seconds.toString() + "秒前"
            }
        } else if (result in 60000..3599999) { // 一小时内
            val seconds = result / 60000
            seconds.toString() + "分钟前"
        } else if (result in 3600000..86399999) { // 一天内
            val seconds = result / 3600000
            seconds.toString() + "小时前"
        } else if (result in 86400000..1702967295) { // 三十天内
            val seconds = result / 86400000
            seconds.toString() + "天前"
        } else { // 日期格式
            val format = "yyyy/MM/dd HH:mm"
            val df = SimpleDateFormat(format, Locale.US)
            df.format(targetDate)
        }
    }

}