package com.airy.v2plus.about

import com.airy.v2plus.R
import com.airy.v2plus.base.BaseActivity

class AboutActivity : BaseActivity() {

    override fun setContentView() {
        setContentView(R.layout.activity_about)
    }

    override fun initViews() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
