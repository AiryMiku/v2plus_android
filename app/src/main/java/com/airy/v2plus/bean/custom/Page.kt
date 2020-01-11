package com.airy.v2plus.bean.custom


/**
 * Created by Airy on 2020-01-11
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

data class Page<T>(
    val current: Long,  // attention index start from 0 or 1
    val min: Long,
    val max: Long,
    val items: List<T>
) {
    fun isLast(): Boolean = current == max

    fun isFirst(): Boolean = current == min
}