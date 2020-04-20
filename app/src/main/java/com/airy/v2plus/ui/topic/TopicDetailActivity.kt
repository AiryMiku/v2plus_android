package com.airy.v2plus.ui.topic

import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.airy.v2plus.Common
import com.airy.v2plus.R
import com.airy.v2plus.bean.official.Reply
import com.airy.v2plus.databinding.ActivityTopicDetailBinding
import com.airy.v2plus.ui.base.BaseActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class TopicDetailActivity : BaseActivity(), TopicDetailAdapter.ViewOfItemOnClickListener {

    private lateinit var binding: ActivityTopicDetailBinding
    private lateinit var viewModel: TopicDetailViewModel
    private lateinit var adapter: TopicDetailAdapter
    private var id: Long = 0L

    override val toolbarLabel: CharSequence? = "Detail"
    override val displayHomeAsUpEnabled: Boolean? = true

    override fun setContentView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_topic_detail)
    }

    override fun initViews() {
        id = intent.getLongExtra(Common.KEY_ID.TOPIC, 0L)
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
        })
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
