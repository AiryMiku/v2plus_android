package com.airy.v2plus.base

import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableBoolean
import com.airy.v2plus.R
import com.airy.v2plus.util.DateUtil
import com.airy.v2plus.util.GlideImageGetter
import com.airy.v2plus.util.TextViewTagHandler
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import java.lang.ref.WeakReference


@BindingAdapter(value = ["visible"], requireAll = false)
fun visible(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter(value = ["visible"], requireAll = false)
fun visible(view: View, visible: ObservableBoolean) {
    view.visibility = if (visible.get()) View.VISIBLE else View.GONE
}

@BindingAdapter(value = ["imageUrl"], requireAll = false)
fun loadImage(imageView: ImageView, imageUrl: String?) {
    if (imageUrl != null) {
        Glide.with(imageView.context)
            .load(imageUrl)
            .placeholder(R.color.color_control_light)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }
}

@BindingAdapter(value = ["topic_num"], requireAll = false)
fun topicNumString(view: TextView, topics: Long?){
    if (topics == null) {
        view.text = ""
    } else {
        view.text = "Topics: ${topics}"
    }
}

@BindingAdapter(value = ["star_num"], requireAll = false)
fun starNumString(view: TextView, stars: Long?){
    if (stars == null) {
        view.text = ""
    } else {
        view.text = "Stars: ${stars}"
    }
}

@BindingAdapter(value = ["format_time"], requireAll = false)
fun formatTime(view: TextView, timeStamp: Long?) {
    if (timeStamp == null) {
        view.text = ""
    } else {
        view.text = DateUtil.formatTime(timeStamp)
    }
}

@BindingAdapter(value = ["html_content"], requireAll = false)
fun setHtmlContent(view: TextView, htmlContent: String?) {
    when {
        htmlContent == null || htmlContent.trim().isBlank() -> {
            view.text = "---No content---"
        }
        else -> {
            view.text = HtmlCompat.fromHtml(
                htmlContent, HtmlCompat.FROM_HTML_MODE_LEGACY,
                GlideImageGetter(view.context, WeakReference(view)),
                TextViewTagHandler(view.context)
            )
            view.movementMethod = LinkMovementMethod.getInstance()
        }
    }
}