package com.airy.v2plus.ui.topic

import android.annotation.SuppressLint
import android.content.Context
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.airy.v2plus.R
import com.airy.v2plus.databinding.ItemHeaderTopicBinding
import com.airy.v2plus.model.official.Reply
import com.airy.v2plus.model.official.Topic
import com.airy.v2plus.databinding.ItemTopicReplyBinding
import com.airy.v2plus.ui.topic.TopicDetailViewHolder.HeaderItemHolder
import com.airy.v2plus.ui.topic.TopicDetailViewHolder.ReplyItemHolder
import com.airy.v2plus.util.DateUtil
import com.airy.v2plus.util.GlideImageGetter
import com.airy.v2plus.util.TextViewTagHandler
import java.lang.ref.WeakReference


/**
 * Created by Airy on 2019-12-16
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

internal class TopicDetailAdapter(
    private val viewOnClickListener: ViewOfItemOnClickListener
) :
    ListAdapter<Any, TopicDetailViewHolder>(TopicDetailDiffCallback) {

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return when (item) {
            is Reply -> R.layout.item_topic_reply
            is Topic -> R.layout.item_header_topic
            else -> throw IllegalStateException("Unknown type: ${item::class.java.simpleName}")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicDetailViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.item_topic_reply -> ReplyItemHolder(
                ItemTopicReplyBinding.inflate(
                    inflater,
                    parent,
                    false
                )
            )
            R.layout.item_header_topic -> HeaderItemHolder(
                ItemHeaderTopicBinding.inflate(
                    inflater,
                    parent,
                    false
                )
            )
            else -> throw IllegalArgumentException("Invalid viewType")
        }
    }

    override fun onBindViewHolder(holder: TopicDetailViewHolder, position: Int) {
        when (holder) {
            is HeaderItemHolder -> {
                val topic = getItem(position) as Topic
                holder.binding.topic = topic
            }
            is ReplyItemHolder -> {
                val reply = getItem(position) as Reply
                holder.binding.reply = reply
                holder.binding.likeLayout.let { ll ->
                    ll.setOnClickListener {
                        holder.binding.likeView.toggle()
                        viewOnClickListener.onThankClickListener(reply)
                    }
                }
                holder.binding.root.setOnLongClickListener {
                    viewOnClickListener.onReplyLongClickListener(reply)
                }
            }
        }
    }


    interface ViewOfItemOnClickListener {
        fun onThankClickListener(reply: Reply)
        fun onReplyLongClickListener(reply: Reply): Boolean
    }
}

internal sealed class TopicDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    class ReplyItemHolder(val binding: ItemTopicReplyBinding) : TopicDetailViewHolder(binding.root)

    class HeaderItemHolder(val binding: ItemHeaderTopicBinding) :
        TopicDetailViewHolder(binding.root)

}

internal object TopicDetailDiffCallback : DiffUtil.ItemCallback<Any>() {
    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
        return when {
            oldItem is Topic && newItem is Topic -> oldItem.id == newItem.id
            oldItem is Reply && newItem is Reply -> oldItem.id == newItem.id
            else -> false
        }
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
        return when {
            oldItem is Topic && newItem is Topic -> Topic == newItem
            oldItem is Reply && newItem is Reply -> Reply == newItem
            else -> true
        }
    }
}
