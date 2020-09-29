package com.airy.v2plus.ui.hot_or_latest

import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.airy.v2plus.Common
import com.airy.v2plus.R
import com.airy.v2plus.databinding.ActivityHotestOrLatestBinding
import com.airy.v2plus.navToTopicActivity
import com.airy.v2plus.base.BaseActivity
import com.airy.v2plus.ui.topic.TopicsAdapter

class HotOrLatestActivity : BaseActivity() {
    override val toolbarLabel: CharSequence? = "Hot"
    override val displayHomeAsUpEnabled: Boolean? = true
    private var isLatest = false

    private val viewModel: HotOrLatestViewModel by viewModels()
    private lateinit var binding: ActivityHotestOrLatestBinding
    private lateinit var adapter: TopicsAdapter

    override fun setContentView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_hotest_or_latest)
    }

    override fun initViews() {
        isLatest = intent.getBooleanExtra(Common.KEY_BOOLEAN.IS_LATEST, false)
        adapter = TopicsAdapter() {
            navToTopicActivity(it.id)
        }
        binding.list.adapter = adapter
        binding.refresh.setOnRefreshListener {
            if (isLatest) {
                viewModel.getLatest()
            } else {
                viewModel.getHot()
            }
            binding.refresh.isRefreshing = true
        }
        subscribeUI()
    }

    override fun loadData() {
        if (isLatest) {
            supportActionBar?.title = "Latest"
            viewModel.getLatest()
        } else {
            viewModel.getHot()
        }
        binding.refresh.isRefreshing = true
    }

    private fun subscribeUI() {
        if (isLatest) {
            viewModel.latest.observe(this, Observer {
                binding.refresh.isRefreshing = false
                adapter.submitList(it)
            })
        } else {
            viewModel.hot.observe(this, Observer {
                binding.refresh.isRefreshing = false
                adapter.submitList(it)
            })
        }
    }
}