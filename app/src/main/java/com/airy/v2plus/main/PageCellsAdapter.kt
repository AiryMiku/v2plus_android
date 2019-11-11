package com.airy.v2plus.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.airy.v2plus.bean.custom.PageCell
import com.airy.v2plus.databinding.ItemPageCellBinding


/**
 * Created by Airy on 2019-07-29
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

class PageCellsAdapter(private val onClickCallback: (PageCell) -> Unit)
    : ListAdapter<PageCell, PageCellsAdapter.ViewHolder>(TaskDiffCallback()){

    class TaskDiffCallback: DiffUtil.ItemCallback<PageCell>() {
        override fun areItemsTheSame(oldItem: PageCell, newItem: PageCell): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: PageCell, newItem: PageCell): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemPageCellBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cell = getItem(position)
        holder.binding.cell = cell
        holder.binding.root.setOnClickListener {
            onClickCallback(cell)
        }
    }

    class ViewHolder(val binding: ItemPageCellBinding): RecyclerView.ViewHolder(binding.root)
}