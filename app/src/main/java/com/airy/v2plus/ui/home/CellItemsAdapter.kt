package com.airy.v2plus.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.airy.v2plus.R
import com.airy.v2plus.bean.custom.CellItem
import com.airy.v2plus.databinding.ItemPageCellBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy


/**
 * Created by Airy on 2019-07-29
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

class CellItemsAdapter(private val context: Context?, private val onClickCallback: (CellItem) -> Unit = {})
    : ListAdapter<CellItem, CellItemsAdapter.ViewHolder>(
    TaskDiffCallback()
){

    class TaskDiffCallback: DiffUtil.ItemCallback<CellItem>() {
        override fun areItemsTheSame(oldItem: CellItem, newItem: CellItem): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: CellItem, newItem: CellItem): Boolean {
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
            Glide.with(it)
                .load("https:" + cell.avatarUrl)
                .format(DecodeFormat.PREFER_RGB_565)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .downsample(DownsampleStrategy.AT_LEAST)
                .dontAnimate()
                .placeholder(R.color.color_control_light)
                .into(holder.binding.avatar)
        }
        holder.binding.root.setOnClickListener {
            onClickCallback(cell)
        }
    }

    class ViewHolder(val binding: ItemPageCellBinding): RecyclerView.ViewHolder(binding.root)
}