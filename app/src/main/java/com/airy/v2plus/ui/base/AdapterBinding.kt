package com.airy.v2plus.ui.base

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableBoolean


@BindingAdapter(value = ["visible"], requireAll = false)
fun visible(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter(value = ["visible"], requireAll = false)
fun visible(view: View, visible: ObservableBoolean) {
    view.visibility = if (visible.get()) View.VISIBLE else View.GONE
}