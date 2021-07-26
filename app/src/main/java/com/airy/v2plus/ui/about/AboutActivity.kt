package com.airy.v2plus.ui.about

import android.view.View
import android.widget.ImageView
import com.airy.v2plus.BuildConfig
import com.airy.v2plus.R
import com.airy.v2plus.base.BaseActivity
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : BaseActivity() {

    override fun setContentView() {
        setContentView(R.layout.activity_about)
    }

    override val toolbarLabel: CharSequence = "About"
    override val displayHomeAsUpEnabled: Boolean = true

    override fun initViews() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // to use object animator, you must add a view in your layout...
        val list = ArrayList<View>()
        list.add(app_logo_shadow)
        app_logo?.followingItemViews = list

        app_name.text = if (BuildConfig.BUILD_TYPE == "debug") getString(R.string.app_name_debug) else getString(R.string.app_name)
        version_code.text = "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"
    }
}
