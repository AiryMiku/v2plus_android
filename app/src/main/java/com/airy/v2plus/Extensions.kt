package com.airy.v2plus

import android.content.Context
import android.content.Intent
import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.airy.v2plus.ui.node.NodeActivity
import com.airy.v2plus.ui.theme.Theme
import com.airy.v2plus.ui.topic.TopicDetailActivity
import com.orhanobut.logger.Logger
import kotlinx.coroutines.*
import org.jetbrains.annotations.Nullable


/**
 * Created by Airy on 2020-01-14
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

fun Context.isNightMode(): Boolean {
    val mode = this.resources.configuration.uiMode and UI_MODE_NIGHT_MASK
    return mode == UI_MODE_NIGHT_YES
}

fun AppCompatActivity.updateForTheme(theme: Theme) {
    val mode = when (theme) {
        Theme.DARK -> AppCompatDelegate.MODE_NIGHT_YES
        Theme.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
        Theme.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        Theme.BATTERY_SAVER -> AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
    }
    delegate.localNightMode = mode
    AppCompatDelegate.setDefaultNightMode(mode)
}

fun Fragment.navToTopicActivity(topicId: Long, @Nullable replyNo: Long? = null) {
    val intent = Intent(this.requireActivity(), TopicDetailActivity::class.java)
    intent.putExtra(Common.KEY_ID.TOPIC_ID, topicId)
    replyNo?.let { intent.putExtra(Common.KEY_ID.REPLY_NO, it) }
    startActivity(intent)
}

fun AppCompatActivity.navToTopicActivity(topicId: Long, @Nullable replyNo: Long? = null) {
    val intent = Intent(this, TopicDetailActivity::class.java)
    intent.putExtra(Common.KEY_ID.TOPIC_ID, topicId)
    replyNo?.let { intent.putExtra(Common.KEY_ID.REPLY_NO, it) }
    startActivity(intent)
}

fun Fragment.navToNodeDetailActivity(nodeName: String) {
    val intent = Intent(this.requireActivity(), NodeActivity::class.java)
    intent.putExtra(Common.KEY_ID.NODE_NAME, nodeName)
    startActivity(intent)
}

// Context
fun Context.showToastShort(message: CharSequence) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.showToastShort(@StringRes message: Int) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.showToastLong(message: CharSequence) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.showToastLong(@StringRes message: Int) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.sendBroadcastBySelf(intent: Intent) {
    this.sendBroadcast(intent.setPackage(this.packageName))
}

const val TAG = "Extensions"

fun launchOnIOInGlobal(
    tryBlock: suspend CoroutineScope.() -> Unit,
    catchBlock: suspend CoroutineScope.(Throwable) -> Unit = { e ->
        Logger.e(TAG, e.message, e)
    },
    finalBlock: suspend CoroutineScope.() -> Unit = {}
): Job {
    return GlobalScope.launch(Dispatchers.IO) {
        try {
            tryBlock()
        } catch (e: Exception) {
            catchBlock(e)
        } finally {
            finalBlock()
        }
    }
}

fun launchOnMainInGlobal(
    tryBlock: suspend CoroutineScope.() -> Unit,
    catchBlock: suspend CoroutineScope.(Throwable) -> Unit = { e ->
        Logger.e(TAG, e.message, e)
    },
    finalBlock: suspend CoroutineScope.() -> Unit = {}
): Job {
    return GlobalScope.launch(Dispatchers.Main) {
        try {
            tryBlock()
        } catch (e: Exception) {
            catchBlock(e)
        } finally {
            finalBlock()
        }
    }
}
