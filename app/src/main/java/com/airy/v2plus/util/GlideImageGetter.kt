package com.airy.v2plus.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.Html
import android.util.Log
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
import com.bumptech.glide.request.Request
import com.bumptech.glide.request.target.SizeReadyCallback
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import java.lang.ref.WeakReference


/**
 * Created by Airy on 2020-01-08
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

class GlideImageGetter(private val context: Context,
                       private val textViewReference: WeakReference<TextView>
): Html.ImageGetter {

    override fun getDrawable(source: String?): Drawable {
        val placeholder = BitmapDrawablePlaceholder()
        Glide.with(context).asBitmap()
            .format(DecodeFormat.PREFER_RGB_565)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .downsample(DownsampleStrategy.AT_LEAST)
            .load(source)
            .thumbnail(0.3f).into(placeholder)
        return placeholder
    }

    inner class BitmapDrawablePlaceholder:
        BitmapDrawable(context.resources, Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565)),
        Target<Bitmap> {

        private var imageDrawable: Drawable? = null
            set(value) {
                if (value != null) {
                    val drawableWidth = value.intrinsicWidth
                    val drawableHeight = value.intrinsicHeight
                    val maxWidth: Int = textViewReference.get()?.measuredWidth ?: 0
                    if (drawableWidth > maxWidth) {
                        val calculatedHeight = maxWidth * drawableHeight / drawableWidth
                        value.setBounds(0, 0, maxWidth, calculatedHeight)
                        setBounds(0, 0, maxWidth, calculatedHeight)
                    } else {
                        value.setBounds(0, 0, drawableWidth, drawableHeight)
                        setBounds(0, 0, drawableWidth, drawableHeight)
                    }
                    textViewReference.get()?.let {
                        it.text = it.text
                    }
                    field = value
                }
            }

        override fun draw(canvas: Canvas) {
            imageDrawable?.draw(canvas)
        }

        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
            imageDrawable = BitmapDrawable(context.resources, resource)
        }

        override fun onLoadCleared(placeholder: Drawable?) {}

        override fun onLoadStarted(placeholder: Drawable?) {
            placeholder?.let {
                imageDrawable = it
            }
        }

        override fun onLoadFailed(errorDrawable: Drawable?) {
            errorDrawable?.let {
                imageDrawable = it
            }
        }

        override fun getSize(cb: SizeReadyCallback) {
            textViewReference.get()?.let {
                it.post {
                    cb.onSizeReady((it.width * 0.8).toInt(), (it.height * 0.8).toInt())
                }
            }
        }

        override fun getRequest(): Request? = null

        override fun onStart() {
            Log.d("GlideImageGetter", "onStart")
        }

        override fun onDestroy() {
            Log.d("GlideImageGetter", "onDestroy")
        }

        override fun onStop() {
            Log.d("GlideImageGetter", "onStop")
        }

        override fun setRequest(request: Request?) {}

        override fun removeCallback(cb: SizeReadyCallback) {
            Log.d("GlideImageGetter", "removeCallback")
        }
    }
}