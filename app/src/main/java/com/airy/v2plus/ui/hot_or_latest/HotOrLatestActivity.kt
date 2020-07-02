package com.airy.v2plus.ui.hot_or_latest

import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.airy.v2plus.Common
import com.airy.v2plus.R
import com.airy.v2plus.databinding.ActivityHotestOrLatestBinding
import com.airy.v2plus.navToTopicActivity
import com.airy.v2plus.ui.base.BaseActivity

class HotOrLatestActivity : BaseActivity() {
    override val toolbarLabel: CharSequence? = "Hot"
    override val displayHomeAsUpEnabled: Boolean? = true
    private var isLatest = false

    private val viewModel by lazy { ViewModelProviders.of(this)[HotOrLatestViewModel::class.java] }
    private lateinit var binding: ActivityHotestOrLatestBinding
    private lateinit var adapter: HotOrLatestAdapter

    override fun setContentView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_hotest_or_latest)
    }

    override fun initViews() {
        isLatest = intent.getBooleanExtra(Common.KEY_BOOLEAN.IS_LATEST, false)
        adapter = HotOrLatestAdapter {
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