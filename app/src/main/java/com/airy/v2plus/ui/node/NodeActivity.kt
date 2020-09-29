package com.airy.v2plus.ui.node

import androidx.databinding.DataBindingUtil
import com.airy.v2plus.Common
import com.airy.v2plus.R
import com.airy.v2plus.databinding.ActivityNodeBinding
import com.airy.v2plus.navToTopicActivity
import com.airy.v2plus.base.BaseActivity
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

    private var nodeName: String? = null

    override fun setContentView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_node)
    }

    override fun initViews() {
        nodeName = intent.getStringExtra(Common.KEY_ID.NODE_NAME)

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
        nodeName?.let {
            viewModel.getNodeDetail(it)
            viewModel.getTopicsByNodeName(it)
        }
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