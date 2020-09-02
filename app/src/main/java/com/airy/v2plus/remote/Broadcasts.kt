package com.airy.v2plus.remote

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.airy.v2plus.Intents
import com.airy.v2plus.bean.custom.Balance

/**
 * Created by Airy on 2020/9/1
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */
object Broadcasts {
    interface Receiver {
        fun onRedeemSuccess(balance: Balance?)
    }

    private val receivers = mutableListOf<Receiver>()

    private val broadcastReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.`package` != context?.packageName) {
                return
            }

            when(intent?.action) {
                Intents.REDEEM_SUCCESS -> {
                    receivers.forEach {
                        val balance = intent.getParcelableExtra<Balance?>("balance")
                        it.onRedeemSuccess(balance)
                    }
                }
            }

        }
    }

    fun register(receiver: Receiver) {
        receivers.add(receiver)
    }

    fun unregister(receiver: Receiver) {
        receivers.remove(receiver)
    }

    fun init(application: Application) {
        ProcessLifecycleOwner.get().lifecycle.addObserver(object: DefaultLifecycleObserver{
            override fun onStart(owner: LifecycleOwner) {
                application.registerReceiver(broadcastReceiver, IntentFilter().apply {
                    addAction(Intents.REDEEM_SUCCESS)
                })
            }

            override fun onStop(owner: LifecycleOwner) {
                application.unregisterReceiver(broadcastReceiver)
            }
        })
    }
}