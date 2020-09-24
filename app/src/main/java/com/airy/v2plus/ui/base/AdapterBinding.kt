package com.airy.v2plus.ui.base

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableBoolean
import com.airy.v2plus.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions


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