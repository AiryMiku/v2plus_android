package com.airy.v2plus

import android.app.Application
import android.content.Context
import kotlin.properties.Delegates


/**
 * Created by Airy on 2019-09-16
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

class V2plusApp: Application() {

    companion object {
        private var instance: V2plusApp by Delegates.notNull()

        fun getAppContext(): Context = instance
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}