package com.airy.v2plus.util


/**
 * Created by Airy on 2019-09-19
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

fun isBlankOrEmpty(vararg strings: String): Boolean {
    for (s in strings) {
        if (s.isEmpty() || s.isBlank()) {
            return true
        }
    }
    return false
}