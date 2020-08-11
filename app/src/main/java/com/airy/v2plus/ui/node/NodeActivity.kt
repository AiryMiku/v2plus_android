package com.airy.v2plus.ui.node

import android.os.Bundle
import com.airy.v2plus.R
import com.airy.v2plus.showToastShort
import com.airy.v2plus.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_node.*

class NodeActivity : BaseActivity() {
    override val toolbarLabel: CharSequence?
        get() = "Node"
    override val displayHomeAsUpEnabled: Boolean?
        get() = true

    override fun setContentView() {
        setContentView(R.layout.activity_node)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        like_view?.setOnClickListener {
            like_view.toggle()
        }
    }
}