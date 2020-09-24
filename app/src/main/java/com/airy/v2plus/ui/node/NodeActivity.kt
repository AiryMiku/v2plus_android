package com.airy.v2plus.ui.node

import androidx.databinding.DataBindingUtil
import com.airy.v2plus.R
import com.airy.v2plus.databinding.ActivityNodeBinding
import com.airy.v2plus.navToTopicActivity
import com.airy.v2plus.ui.base.BaseActivity
import com.airy.v2plus.ui.topic.TopicsAdapter
import com.google.android.material.appbar.AppBarLayout
import org.koin.androidx.viewmodel.ext.android.viewModel

class NodeActivity : BaseActivity() {

    override val toolbarLabel: CharSequence?
        get() = ""
    override val displayHomeAsUpEnabled: Boolean?
        get() = true

    private lateinit var binding: ActivityNodeBinding

    private val viewModel by viewModel<NodeDetailViewModel>()
    private lateinit var adapter: TopicsAdapter

    override fun setContentView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_node)
    }

    override fun initViews() {
        binding.appBarLayout.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
                binding.titleTextView.alpha = -verticalOffset.toFloat() / appBarLayout.totalScrollRange
            })
        adapter = TopicsAdapter() {
            navToTopicActivity(it.id)
        }
        binding.topics.adapter = adapter
        subscribeUI()
    }

    override fun loadData() {
        super.loadData()
        viewModel.getNodeDetail("v2ex")
        viewModel.getTopicsByNodeName("v2ex")
    }

    private fun subscribeUI() {
        viewModel.node.observe(this) {
            binding.node = it
        }
        viewModel.topics.observe(this) {
            adapter.submitList(it)
        }
    }
}