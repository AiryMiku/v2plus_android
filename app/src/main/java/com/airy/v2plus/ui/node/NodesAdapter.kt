package com.airy.v2plus.ui.node

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.airy.v2plus.bean.official.Node
import com.airy.v2plus.databinding.ItemNodeBinding


/**
 * Created by Airy on 2019-11-07
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

class NodesAdapter(private val onClickCallback: ((Node) -> Unit)? = null,
                   private val onLongClickCallback: ((Node) -> Unit)? = null)
    : PagedListAdapter<Node, NodesAdapter.ViewHolder>(diffCallback){

    companion object {
        private val diffCallback = object: DiffUtil.ItemCallback<Node>() {
            override fun areItemsTheSame(oldItem: Node, newItem: Node): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Node, newItem: Node): Boolean {
                return oldItem == newItem
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemNodeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { n ->
            holder.binding.node = n
            holder.binding.root.apply {
                setOnClickListener {
                    onClickCallback?.invoke(n)
                }
                setOnLongClickListener {
                    onLongClickCallback?.invoke(n)
                    true
                }
            }
        }
    }

    class ViewHolder(val binding: ItemNodeBinding): RecyclerView.ViewHolder(binding.root)
}