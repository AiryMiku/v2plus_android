package com.airy.v2plus.util

import android.graphics.drawable.Drawable
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory

object ViewUtil {

}

private val crossFadeOptions = run {
    val builder = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true)
    DrawableTransitionOptions.withCrossFade(builder)
}

fun RequestBuilder<Drawable>.withCrossFade(): RequestBuilder<Drawable> {
    return this.transition(crossFadeOptions)
}