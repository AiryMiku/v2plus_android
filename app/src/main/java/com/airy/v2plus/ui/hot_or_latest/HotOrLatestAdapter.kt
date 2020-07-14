package com.airy.v2plus.ui.hot_or_latest

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.airy.v2plus.bean.official.Topic
import com.airy.v2plus.databinding.ItemTopicBinding
import com.airy.v2plus.loadAvatar
import com.airy.v2plus.loadTopicImage

class HotOrLatestAdapter(private val context: Context,
                         private val onClickCallback: (Topic) -> Unit = {})
    : ListAdapter<Topic, HotOrLatestAdapter.ViewHolder>(
    TaskDiffCallback()
){

    class TaskDiffCallback: DiffUtil.ItemCallback<Topic>() {
        override fun areItemsTheSame(oldItem: Topic, newItem: Topic): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Topic, newItem: Topic): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemTopicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val t = getItem(position)
        holder.binding.topic = t
        holder.binding.avatar.loadAvatar(context, t.member.avatarMiniUrl)
        holder.binding.root.setOnClickListener {
            onClickCallback(t)
        }
    }

    class ViewHolder(val binding: ItemTopicBinding): RecyclerView.ViewHolder(binding.root)
}