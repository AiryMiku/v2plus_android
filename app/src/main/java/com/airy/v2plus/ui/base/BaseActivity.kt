package com.airy.v2plus.ui.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.airy.v2plus.App
import com.airy.v2plus.R
import com.airy.v2plus.ui.share.ShareViewModel
import com.airy.v2plus.ui.theme.Theme
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import java.lang.IllegalArgumentException


/**
 * Created by Airy on 2019-08-29
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

abstract class BaseActivity: AppCompatActivity(), CoroutineScope by MainScope() {

    protected var toolbar: Toolbar? = null
    // default label
    abstract val toolbarLabel: CharSequence?
    abstract val displayHomeAsUpEnabled: Boolean? //Todo: need refactor

    protected var shareViewModel: ShareViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // init share viewModel
        shareViewModel = getAppViewModelProvider()?.get(ShareViewModel::class.java)
        updateForTheme()
        setContentView()
        initToolbar()
        initViews()
        loadData()
    }

    open fun initViews() {}

    open fun loadData() {}

    abstract fun setContentView()

    private fun initToolbar() {
        toolbar = findViewById(R.id.toolbar)
        toolbar?.let { setSupportActionBar(it) }
    }

    override fun setSupportActionBar(toolbar: Toolbar?) {
        super.setSupportActionBar(toolbar)
        supportActionBar?.let { actionBar ->
            displayHomeAsUpEnabled?.let { actionBar.setDisplayHomeAsUpEnabled(it) }
            actionBar.title = toolbarLabel
        }
    }

    fun navToActivity(context: Context, clazz: Class<*>) {
        startActivity(Intent(context, clazz))
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun makeToastShort(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun makeToastLong(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    fun makeSnackBarShort(layout: View, message: String) {
        Snackbar.make(layout, message, Snackbar.LENGTH_SHORT).show()
    }

    fun makeSnackBarLong(layout: View, message: String) {
        Snackbar.make(layout, message, Snackbar.LENGTH_LONG).show()
    }

    private fun getAppViewModelProvider(): ViewModelProvider? {
        return (applicationContext as App).getAppViewModelProvider(this)
    }

    override fun attachBaseContext(newBase: Context?) {
        val base = newBase ?: return super.attachBaseContext(newBase)

        // do something
        super.attachBaseContext(newBase)
    }

    private fun updateForTheme() {
        shareViewModel?.let {
            it.theme.observe(this, Observer { t ->
                when(t) {
                    Theme.LIGHT -> delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
                    Theme.DARK -> delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
                    Theme.SYSTEM -> delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                    Theme.BATTERY_SAVER -> delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
                    else -> {}     // do nothing
                }
            })
        }
    }
}