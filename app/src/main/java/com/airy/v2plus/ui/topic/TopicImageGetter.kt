package com.airy.v2plus.ui.topic

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.Html
import android.util.Log
import android.widget.TextView
import com.airy.v2plus.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import java.lang.ref.WeakReference


/**
 * Created by Airy on 2020-01-08
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

class TopicImageGetter(private val context: Context,
                       private val textViewReference: WeakReference<TextView>): Html.ImageGetter {

    override fun getDrawable(source: String?): Drawable {
        Log.d("TopicImageGetter", "source -> $source")
        val imageDrawable = BitmapDrawable()
        val urlDrawable = Glide.with(context).load(source).placeholder(R.color.color_control_light).submit()
        return urlDrawable.get()
    }

    inner class TopicDrawableWrapper(private val drawable: Drawable): CustomTarget<BitmapDrawable>() {
        override fun onLoadCleared(placeholder: Drawable?) {}

        override fun onResourceReady(
            resource: BitmapDrawable,
            transition: Transition<in BitmapDrawable>?
        ) {
        }

    }
}