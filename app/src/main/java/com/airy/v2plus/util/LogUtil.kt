package com.airy.v2plus.util

import com.airy.v2plus.BuildConfig
import timber.log.Timber

/**
 * Created by Airy on 2020/9/23
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */
object LogUtil {

    fun init() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

    }

    fun d(t: Throwable? = null, msg: String, vararg str: String) {
        Timber.d(t)
    }
}