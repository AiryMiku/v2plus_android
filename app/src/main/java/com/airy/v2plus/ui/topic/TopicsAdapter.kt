package com.airy.v2plus.ui.topic

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.airy.v2plus.databinding.ItemTopicBinding
import com.airy.v2plus.model.official.Topic

class TopicsAdapter(
    private val onClickCallback: ((Topic) -> Unit)?
) : ListAdapter<Topic, TopicsAdapter.ViewHolder>(
    TopicDiffCallback
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemTopicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val t = getItem(position)
        holder.binding.topic = t
        holder.binding.root.setOnClickListener {
            onClickCallback?.invoke(t)
        }
    }

    class ViewHolder(val binding: ItemTopicBinding) : RecyclerView.ViewHolder(binding.root)
}

internal object TopicDiffCallback : DiffUtil.ItemCallback<Topic>() {
    override fun areItemsTheSame(oldItem: Topic, newItem: Topic): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Topic, newItem: Topic): Boolean {
        return oldItem == newItem
    }
}