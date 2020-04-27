package com.airy.v2plus

import android.content.Context
import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.airy.v2plus.ui.theme.Theme
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy


/**
 * Created by Airy on 2020-01-14
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

// glide
fun ImageView.loadLowQualityImageWithPlaceholder(url: String?) {
    url?.let {
        Glide.with(context)
        .load(it)
        .format(DecodeFormat.PREFER_RGB_565)
        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        .downsample(DownsampleStrategy.AT_LEAST)
        .dontAnimate()
        .placeholder(R.color.color_control_light)
        .into(this)
    }
}

fun Context.isNightMode(): Boolean {
    val mode = this.resources.configuration.uiMode and UI_MODE_NIGHT_MASK
    return mode == UI_MODE_NIGHT_YES
}

fun AppCompatActivity.updateForTheme(theme: Theme) = when (theme) {
    Theme.DARK -> delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
    Theme.LIGHT -> delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
    Theme.SYSTEM -> delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    Theme.BATTERY_SAVER -> delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
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
