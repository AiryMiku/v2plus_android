package com.airy.v2plus.bean.custom

import androidx.paging.PagedList


/**
 * Created by Airy on 2020-01-16
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

data class Paged<T>(
    val current: Int,  // attention index start from 0 or 1
    val min: Int,
    val max: Int,
    val items: PagedList<T>
    ) {
        fun isLast(): Boolean = current == max

        fun isFirst(): Boolean = current == min
}