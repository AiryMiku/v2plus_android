package com.airy.v2plus.api

import com.airy.v2plus.Config

object RequestHelper {
    fun getCaptchaImageUrl(once: String): String = "${Config.BASE_URL}/_captcha?once=$once"
}