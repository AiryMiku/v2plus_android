package com.airy.v2plus.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.airy.v2plus.bean.custom.MainPageItem
import com.airy.v2plus.databinding.ItemPageCellBinding
import com.airy.v2plus.loadLowQualityImageWithPlaceholder


/**
 * Created by Airy on 2019-07-29
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

class MainPageItemsAdapter(private val context: Context?,
                           private val onClickCallback: (MainPageItem, ViewHolder) -> Unit = { _, _ ->})
    : ListAdapter<MainPageItem, MainPageItemsAdapter.ViewHolder>(
    TaskDiffCallback()
){

    class TaskDiffCallback: DiffUtil.ItemCallback<MainPageItem>() {
        override fun areItemsTheSame(oldItem: MainPageItem, newItem: MainPageItem): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: MainPageItem, newItem: MainPageItem): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemPageCellBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cell = getItem(position)
        holder.binding.cell = cell
        context?.let {
            loadLowQualityImageWithPlaceholder(it, "https:" + cell.avatarUrl, holder.binding.avatar)
        }
        holder.binding.root.setOnClickListener {
            onClickCallback(cell, holder)
        }
    }

    class ViewHolder(val binding: ItemPageCellBinding): RecyclerView.ViewHolder(binding.root)
}