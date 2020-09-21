package com.airy.v2plus.util

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.Html
import android.view.View
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import com.airy.v2plus.App
import com.airy.v2plus.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
import com.bumptech.glide.request.Request
import com.bumptech.glide.request.target.SizeReadyCallback
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.util.Preconditions
import com.orhanobut.logger.Logger
import java.lang.ref.WeakReference


/**
 * Created by Airy on 2020-01-08
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

const val TAG = "GlideImageGetter"

class GlideImageGetter(
    private val context: Context,
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

    class LoadingDrawable: Drawable() {

        private var DRAWABLE_LOADING: Drawable?
        private var DRAWABLE_BOUNS: Rect
        private val PAINT by lazy { Paint() }

        private var targetDrawable: Drawable? = null

        init {
            DRAWABLE_LOADING = ContextCompat.getDrawable(App.getAppContext(), R.drawable.ic_baseline_sync_blue_24dp)
            Preconditions.checkNotNull(DRAWABLE_LOADING)
            DRAWABLE_BOUNS = Rect(0, 0, DRAWABLE_LOADING!!.intrinsicWidth, DRAWABLE_LOADING!!.intrinsicHeight)
            DRAWABLE_LOADING!!.bounds = DRAWABLE_BOUNS
            PAINT.setColor(Color.LTGRAY)
            setBounds(DRAWABLE_BOUNS)
        }

        override fun draw(canvas: Canvas) {

            targetDrawable?.let {
                it.draw(canvas)
                return
            }

            canvas.drawRect(bounds, PAINT)
            DRAWABLE_LOADING?.draw(canvas)
        }


        override fun getOpacity(): Int {
            // todo fixed deprecated
            targetDrawable?.let {
                return it.opacity
            }

            return DRAWABLE_LOADING!!.opacity
        }

        fun setDrawable(drawable: Drawable?) {
            targetDrawable = drawable
            val bouns = if (targetDrawable == null) {
                DRAWABLE_BOUNS
            } else {
                targetDrawable!!.bounds
            }
            setBounds(bounds)
        }

        override fun setAlpha(alpha: Int) { }

        override fun setColorFilter(colorFilter: ColorFilter?) { }
    }

    class LoadingDrawableTarget(
        @NonNull protected val view: View,
        private val drawable: LoadingDrawable): Target<Drawable> {

        private var request: Request? = null

        override fun onLoadStarted(placeholder: Drawable?) {
            drawable.setDrawable(placeholder)
        }

        override fun onLoadFailed(errorDrawable: Drawable?) {
            // todo
        }

        override fun getSize(cb: SizeReadyCallback) {
            TODO("Not yet implemented")
        }

        override fun getRequest(): Request? = this.request

        override fun setRequest(request: Request?) {
            this.request = request
        }

        override fun removeCallback(cb: SizeReadyCallback) {
            TODO("Not yet implemented")
        }

        override fun onLoadCleared(placeholder: Drawable?) {
            TODO("Not yet implemented")
        }

        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
            TODO("Not yet implemented")
        }

        override fun onStart() { Logger.d(TAG, "onStart") }
        override fun onStop() { Logger.d(TAG, "onStop") }
        override fun onDestroy() { Logger.d(TAG, "onDestroy") }
    }

    inner class BitmapDrawablePlaceholder:
        BitmapDrawable(context.resources, Bitmap.createBitmap(400, 400, Bitmap.Config.RGB_565)),
        Target<Bitmap> {

        val DRAWABLE_LOADING = ContextCompat.getDrawable(App.getAppContext(), R.drawable.ic_baseline_sync_blue_24dp)
        val PAINT = Paint()

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

        init {
            PAINT.setColor(Color.LTGRAY)
            Preconditions.checkNotNull(DRAWABLE_LOADING)
            DRAWABLE_LOADING?.also {
                setBounds(0, 0, it.intrinsicWidth, it.intrinsicHeight)
            }
        }

        override fun draw(canvas: Canvas) {
            if (imageDrawable != null) {
                imageDrawable?.draw(canvas)
                return
            }
            canvas.drawRect(bounds, PAINT)
//            DRAWABLE_LOADING?.draw(canvas)
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
                    cb.onSizeReady(it.width, it.height)
                }
            }
        }

        override fun getRequest(): Request? = null

        override fun onStart() {
            Logger.d(TAG, "onStart")
        }

        override fun onDestroy() {
            Logger.d(TAG, "onDestroy")
        }

        override fun onStop() {
            Logger.d(TAG, "onStop")
        }

        override fun setRequest(request: Request?) {}

        override fun removeCallback(cb: SizeReadyCallback) {
            Logger.d(TAG, "removeCallback")
        }
    }
}