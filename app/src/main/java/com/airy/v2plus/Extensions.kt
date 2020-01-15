package com.airy.v2plus

import android.content.Context
import android.widget.ImageView
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