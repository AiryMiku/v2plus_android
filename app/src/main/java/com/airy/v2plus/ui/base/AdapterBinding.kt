package com.airy.v2plus.ui.base

import android.view.View
import android.widget.ImageView
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
    when {
        !imageUrl.isNullOrEmpty() -> {
            Glide.with(imageView.context)
                .load(imageUrl)
                .placeholder(R.color.color_control_light)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView)
        }
    }
}