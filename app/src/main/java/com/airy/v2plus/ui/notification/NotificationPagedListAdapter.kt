package com.airy.v2plus.ui.notification

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.airy.v2plus.bean.Status
import com.airy.v2plus.bean.custom.Notification
import com.airy.v2plus.databinding.ItemNotificationBinding
import com.airy.v2plus.loadLowQualityImageWithPlaceholder


/**
 * Created by Airy on 2020-01-15
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

class NotificationPagedListAdapter(private val onClickCallback: (Notification) -> Unit = {})
    : PagedListAdapter<Notification, NotificationPagedListAdapter.ViewHolder>(TaskDiffCallback()) {


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
        if (n != null) {
            holder.binding.notification = n
            holder.binding.avatar.loadLowQualityImageWithPlaceholder(n.avatarUrl)
            holder.binding.root.setOnClickListener {
                onClickCallback(n)
            }
        } else {
            Log.d("NotificationAdapter", "null data found")
        }

    }

    class ViewHolder(val binding: ItemNotificationBinding): RecyclerView.ViewHolder(binding.root)
}