package com.airy.v2plus.ui.topic

import androidx.databinding.DataBindingUtil
import com.airy.v2plus.R
import com.airy.v2plus.databinding.ActivityTopicDetailBinding
import com.airy.v2plus.ui.base.BaseActivity

class TopicDetailActivity : BaseActivity() {

    private lateinit var binding: ActivityTopicDetailBinding

    override fun setContentView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_topic_detail)
    }

    override fun initViews() {

    }

}
