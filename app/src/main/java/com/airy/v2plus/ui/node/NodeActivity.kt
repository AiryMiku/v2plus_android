package com.airy.v2plus.ui.node

import android.os.Bundle
import com.airy.v2plus.App
import com.airy.v2plus.R
import com.airy.v2plus.db.V2plusDb
import com.airy.v2plus.showToastShort
import com.airy.v2plus.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_node.*
import kotlinx.coroutines.*

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

        test_notification?.setOnClickListener {
            launch(Dispatchers.IO) {
                val dao = V2plusDb.getDb(App.getAppContext()).notificationDao()
                dao.getAllNotifications()
            }
        }
    }
}