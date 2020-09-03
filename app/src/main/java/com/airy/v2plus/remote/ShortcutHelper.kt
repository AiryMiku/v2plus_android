package com.airy.v2plus.remote

import android.content.Context
import android.content.Intent
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.airy.v2plus.R
import com.airy.v2plus.service.ServiceActivity

/**
 * Created by Airy on 2020/9/3
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */
object ShortcutHelper {

    private var context: Context? = null

    fun init(context: Context) {
        ShortcutHelper.context = context
    }

    fun addRedeemShortcut() {
        context?.let {
            val shortcutInfo = ShortcutInfoCompat.Builder(it, "Redeem")
                .setShortLabel("Redeem")
                .setLongLabel("Redeem")
                .setIcon(IconCompat.createWithResource(it, R.drawable.ic_redeem_small))
                .setIntent(Intent(Intent.ACTION_VIEW, null, it, ServiceActivity::class.java))
                .build()

            ShortcutManagerCompat.addDynamicShortcuts(it, listOf(shortcutInfo))
        }
    }

    fun removeRedeemShortcut() {
        context?.let {
            ShortcutManagerCompat.removeDynamicShortcuts(it, listOf("Redeem"))
        }
    }
}