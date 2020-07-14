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
import com.airy.v2plus.bean.official.Reply
import com.airy.v2plus.bean.official.Topic
import com.airy.v2plus.databinding.ItemTopicDetailBinding
import com.airy.v2plus.databinding.ItemTopicReplyBinding
import com.airy.v2plus.loadAvatar
import com.airy.v2plus.loadTopicImage
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

internal class TopicDetailAdapter(private val context: Context,
                                  private val viewOnClickListener: ViewOfItemOnClickListener):
    ListAdapter<Any, TopicDetailViewHolder>(TopicDetailDiffCallback) {

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return when (item) {
            is Reply -> R.layout.item_topic_reply
            is Topic -> R.layout.item_topic_detail
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
                    false)
            )
            R.layout.item_topic_detail -> HeaderItemHolder(
                ItemTopicDetailBinding.inflate(
                    inflater,
                    parent,
                    false)
            )
            else -> throw IllegalArgumentException("Invalid viewType")
        }
    }

    override fun onBindViewHolder(holder: TopicDetailViewHolder, position: Int) {
        when (holder) {
            is ReplyItemHolder -> {
                val reply = getItem(position) as Reply
                holder.binding.reply = reply
                holder.binding.avatar.loadAvatar(context, reply.member.avatarMiniUrl)
                holder.binding.createTime.text = DateUtil.formatTime(reply.created)
                reply.contentHtml?.run {
                    holder.binding.replyContent.let {
                        it.text = HtmlCompat.fromHtml(reply.contentHtml, FROM_HTML_MODE_LEGACY,
                            GlideImageGetter(context, WeakReference(it)),
                            TextViewTagHandler(context))
                    }
                }
                holder.binding.replyContent.movementMethod = LinkMovementMethod.getInstance()
                holder.binding.thankNum.let { tv ->
                    tv.setOnClickListener {
                        viewOnClickListener.onThankClickListener(reply)
                    }
                }
                holder.binding.root.setOnLongClickListener {
                    viewOnClickListener. onReplyLongClickListener(reply)
                }
            }
            is HeaderItemHolder -> {
                val topic = getItem(position) as Topic
                holder.binding.topic = topic
                holder.binding.avatar.loadAvatar(context, topic.member.avatarMiniUrl)
                topic.contentHtml?.run {
                    var content = this
                    if (trim().isBlank()) {
                        content = "---No content---\n"
                    }
                    holder.binding.contentText.let {
                        it.text = HtmlCompat.fromHtml(
                            content, FROM_HTML_MODE_LEGACY,
                            GlideImageGetter(context, WeakReference(it)),
                            TextViewTagHandler(context)
                        )
                    }
                }
                holder.binding.contentText.movementMethod = LinkMovementMethod.getInstance()
                holder.binding.createTime.text = DateUtil.formatTime(topic.created)
            }
        }
    }



    interface ViewOfItemOnClickListener {
        fun onThankClickListener(reply: Reply)
        fun onReplyLongClickListener(reply: Reply): Boolean
    }
}

internal sealed class TopicDetailViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    class ReplyItemHolder(val binding: ItemTopicReplyBinding): TopicDetailViewHolder(binding.root)

    class HeaderItemHolder(val binding: ItemTopicDetailBinding): TopicDetailViewHolder(binding.root)

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
