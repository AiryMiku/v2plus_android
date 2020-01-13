package com.airy.v2plus.ui.notification

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.airy.v2plus.R
import com.airy.v2plus.bean.custom.Notification
import com.airy.v2plus.databinding.ItemNotificationBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy


/**
 * Created by Airy on 2020-01-13
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

class NotificationsAdapter(private val context: Context?, private val onClickCallback: (Notification) -> Unit = {})
    : ListAdapter<Notification, NotificationsAdapter.ViewHolder>(
    TaskDiffCallback()
){

    class TaskDiffCallback: DiffUtil.ItemCallback<Notification>() {
        override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val n = getItem(position)
        holder.binding.notification = n
        context?.let {
            Glide.with(it)
                .load("https:" + n.avatarUrl)
                .format(DecodeFormat.PREFER_RGB_565)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .downsample(DownsampleStrategy.AT_LEAST)
                .dontAnimate()
                .placeholder(R.color.color_control_light)
                .into(holder.binding.avatar)
        }
        holder.binding.root.setOnClickListener {
            onClickCallback(n)
        }
    }

    class ViewHolder(val binding: ItemNotificationBinding): RecyclerView.ViewHolder(binding.root)
}