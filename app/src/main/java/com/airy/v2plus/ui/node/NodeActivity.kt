package com.airy.v2plus.ui.node

import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.airy.v2plus.R
import com.airy.v2plus.databinding.ActivityNodeBinding
import com.airy.v2plus.ui.base.BaseActivity
import com.google.android.material.appbar.AppBarLayout

class NodeActivity : BaseActivity() {

    override val toolbarLabel: CharSequence?
        get() = ""
    override val displayHomeAsUpEnabled: Boolean?
        get() = true

    private lateinit var binding: ActivityNodeBinding

    private val viewModel by viewModels<NodeDetailViewModel>()

    override fun setContentView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_node)
    }

    override fun initViews() {
        binding.appBarLayout.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
                binding.titleTextView.alpha = -verticalOffset.toFloat() / appBarLayout.totalScrollRange
            })
    }
}