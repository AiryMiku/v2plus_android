package com.airy.v2plus.service

import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.airy.v2plus.Intents
import com.airy.v2plus.R
import com.airy.v2plus.model.custom.Balance
import com.airy.v2plus.network.RequestHelper
import com.airy.v2plus.repository.UserRepository
import com.airy.v2plus.sendBroadcastBySelf
import com.airy.v2plus.util.UserCenter
import com.airy.v2plus.util.V2exHtmlUtil
import com.orhanobut.logger.Logger
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Created by Airy on 2020/8/29
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */
class RedeemService: IntentService("RedeemService") {

    companion object {
        const val CHANNEL_ID = "REDEEM_NOTIFICATION"
        const val CHANNEL_NAME = "REDEEM_CHANNEL"
        const val NOTIFICATION_ID = 1
    }

    private val TAG = "RedeemService"

    private val networkScope = RequestHelper.networkScope
    private val repository = UserRepository.getInstance()

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        showStartNotification()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onHandleIntent(intent: Intent?) {
        networkScope.launch {
            try {
                if (RequestHelper.isCookieExpired() && UserCenter.getUserId() != 0L) {
                    showFailedNotification("Session Timeout, please re-login.")
                }

                var response = repository.getDailyMissionResponse()
                val param = V2exHtmlUtil.getDailyMissionParam(response)
                delay(2000L)
                if (param.isNotBlank()) {
                    response = repository.getDailyMissionRedeemResponse(param)
                }
                val message = V2exHtmlUtil.getDailyMissionRedeemResult(response)
                showFinishNotification("${message.getOrNull(1)}\n${message.getOrNull(2)}")

                @Suppress("NAME_SHADOWING")
                val intent = Intent(Intents.REDEEM_SUCCESS).apply {
                    val balance = parseBalance(response)
                    val bundle = Bundle()
                    bundle.putParcelable("balance", balance)
                    putExtras(bundle)
                }
                sendBroadcastBySelf(intent)
                // todo need refactor by checking process
            } catch (e: Exception) {
                Logger.e(TAG, e.message, e)
                showFailedNotification()
            }
        }
    }

    private fun parseBalance(response: String): Balance {
        return V2exHtmlUtil.getBalance(response).apply {
            UserCenter.setLastBalance(this)
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = CHANNEL_NAME
            val descriptionText = "This channel is for redeem"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showStartNotification(){
        val builder = createBaseNotificationBuilder(
            subText = "Redeem Start",
            contentText = "Start get your redeem..."
        )

        with(NotificationManagerCompat.from(this)) {
            notify(NOTIFICATION_ID, builder.build())
        }
    }

    private fun showFinishNotification(message: String) {
        val builder = createBaseNotificationBuilder(
            subText = "Redeem Success",
            contentText = "Success: $message"
        ).setTimeoutAfter(4000L)

        with(NotificationManagerCompat.from(this)) {
            notify(NOTIFICATION_ID, builder.build())
        }
    }

    private fun showFailedNotification(cause: String = "Failed to get your redeem") {
        val builder = createBaseNotificationBuilder(
            subText = "Redeem Failed",
            contentText = cause
        )

        with(NotificationManagerCompat.from(this)) {
            notify(NOTIFICATION_ID, builder.build())
        }
    }

    private fun createBaseNotificationBuilder(
        subText: CharSequence? = null,
        contentTitle: CharSequence? = null,
        contentText: CharSequence? = null
    ): NotificationCompat.Builder =
        NotificationCompat.Builder(this, CHANNEL_ID)
            .apply {
                setSmallIcon(R.drawable.ic_redeem_small)
                setSubText(subText)
                setContentTitle(contentTitle)
                setContentText(contentText)
                setWhen(System.currentTimeMillis())
                priority = NotificationCompat.PRIORITY_DEFAULT
                setCategory(NotificationCompat.CATEGORY_MESSAGE)
                setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
            }
}