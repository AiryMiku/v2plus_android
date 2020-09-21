package com.airy.v2plus.service

import android.app.Activity
import android.content.Intent
import android.os.Bundle

class ServiceActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent(this, RedeemService::class.java)
        startService(intent)
        finish()
    }
}