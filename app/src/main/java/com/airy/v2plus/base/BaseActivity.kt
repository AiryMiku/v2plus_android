package com.airy.v2plus.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


/**
 * Created by Airy on 2019-08-29
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

abstract class BaseActivity: AppCompatActivity() {

    val TAG = this::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView()
    }

    abstract fun setContentView()

    fun navToActivity(context: Context, clazz: Class<*>) {
        startActivity(Intent(context, clazz))
    }
}