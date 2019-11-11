package com.airy.v2plus.node

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.airy.v2plus.V2plusApp
import com.airy.v2plus.bean.official.Node
import com.airy.v2plus.databinding.ItemNodeBinding
import com.bumptech.glide.Glide


/**
 * Created by Airy on 2019-11-07
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

class NodesAdapter(private val onClickCallback: (Node) -> Unit)
    : ListAdapter<Node, NodesAdapter.ViewHolder>(TaskDiffCallback()){

    class TaskDiffCallback: DiffUtil.ItemCallback<Node>() {
        override fun areItemsTheSame(oldItem: Node, newItem: Node): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Node, newItem: Node): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemNodeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val node = getItem(position)
        holder.binding.node = node
        Glide.with(V2plusApp.getAppContext()).load(node.avatarNormalUrl).into(holder.binding.nodeImage)
        holder.binding.root.setOnClickListener {
            onClickCallback(node)
        }
    }

    class ViewHolder(val binding: ItemNodeBinding): RecyclerView.ViewHolder(binding.root)
}