package com.airy.v2plus.ui.topic

import android.util.Log
import androidx.core.view.children
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.coroutineScope
import com.airy.v2plus.Common
import com.airy.v2plus.R
import com.airy.v2plus.bean.official.Reply
import com.airy.v2plus.databinding.ActivityTopicDetailBinding
import com.airy.v2plus.ui.base.BaseActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TopicDetailActivity : BaseActivity(), TopicDetailAdapter.ViewOfItemOnClickListener {

    private lateinit var binding: ActivityTopicDetailBinding
    private lateinit var viewModel: TopicDetailViewModel
    private lateinit var adapter: TopicDetailAdapter
    private var id: Long = 0L

    private val TAG = "TopicDetailActivity"

    override val toolbarLabel: CharSequence? = "Detail"
    override val displayHomeAsUpEnabled: Boolean? = true

    override fun setContentView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_topic_detail)
    }

    override fun initViews() {
        id = intent.getLongExtra(Common.KEY_ID.TOPIC_ID, 0L)
        viewModel = ViewModelProviders.of(this).get(TopicDetailViewModel::class.java)   //Todo: need refactor
        adapter = TopicDetailAdapter(this, this)
        binding.list.adapter = adapter
        subscribeUI()
    }

    override fun loadData() {
        if (id != 0L) {
            viewModel.getTopicDetails(id)
            binding.refresh.isRefreshing = true
        } else {
            viewModel.error.value = IllegalArgumentException("Illegal topicId!")
        }
    }

    private fun subscribeUI() {
        viewModel.error.observe(this, Observer {
            makeToastLong(it.toString())
        })
        viewModel.topicDetails.observe(this, Observer {
            adapter.submitList(it)
            binding.refresh.isRefreshing = false
            handlingReplyPositionItem()
        })
    }

    // Todo out of index, because recycle only show display item view, so that way to find adapter position item is not correct
    private fun handlingReplyPositionItem() {
        lifecycle.coroutineScope.launch(Dispatchers.IO) {
            val replyNo = intent.getLongExtra(Common.KEY_ID.REPLY_NO, -1L)
            if (replyNo != -1L) {
                delay(500L)
                val position = replyNo.toInt() + 1  // headItem
                Log.d(TAG, "handle position $position")
                if (position <= adapter.itemCount) {
                    binding.list.smoothScrollToPosition(position)
                    delay(2000L)
                    val displayChildren = binding.list.children
                    displayChildren.iterator().forEach { view ->
                        val adapterPosition = binding.list.getChildAdapterPosition(view)
                        if(adapterPosition == position) {
                            Log.d(TAG, "performClick adapterPosition: $adapterPosition")
//                            view.performClick() todo not correct way to simulate finger click or just add some animation
//                            view.animate()
                        }
                    }
                }
            }
        }
    }

    override fun onThankClickListener(reply: Reply) {
        makeSnackBarShort(binding.container, "Thanks for your admired❤, id: ${reply.id}")
    }

    override fun onReplyLongClickListener(reply: Reply): Boolean {
        val listDialog = MaterialAlertDialogBuilder(this)
        listDialog.setItems(R.array.reply_items) {
                _, which ->
            when (which) {
                0 -> makeToastShort("Dev~")
                1 -> makeToastShort("Dev~")
                2 -> makeToastShort("HoHoHo~")
            }
        }
        listDialog.show()
        return true
    }

}
