package com.airy.v2plus

import android.content.Context
import android.widget.ImageView
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
fun loadLowQualityImageWithPlaceholder(context: Context, url: String, imageView: ImageView) {
    Glide.with(context)
        .load(url)
        .format(DecodeFormat.PREFER_RGB_565)
        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        .downsample(DownsampleStrategy.AT_LEAST)
        .dontAnimate()
        .placeholder(R.color.color_control_light)
        .into(imageView)
}


fun AppCompatActivity.updateForTheme(theme: Theme) = when (theme) {
    Theme.DARK -> delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
    Theme.LIGHT -> delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
    Theme.SYSTEM -> delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    Theme.BATTERY_SAVER -> delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
}