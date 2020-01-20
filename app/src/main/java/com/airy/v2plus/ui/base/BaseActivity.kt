package com.airy.v2plus.ui.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.airy.v2plus.App
import com.airy.v2plus.R
import com.airy.v2plus.ui.share.ShareViewModel
import com.google.android.material.snackbar.Snackbar


/**
 * Created by Airy on 2019-08-29
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

abstract class BaseActivity: AppCompatActivity() {

    private var toolbar: Toolbar? = null

    protected lateinit var shareViewModel: ShareViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView()

        shareViewModel = getAppViewModelProvider()[ShareViewModel::class.java]

        toolbar = findViewById(R.id.toolbar)
        toolbar?.let {
            setSupportActionBar(it)
        }

        initViews()
        loadData()
    }

    open fun initViews() {}

    open fun loadData() {}

    abstract fun setContentView()

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

    fun makeSnackarShort(layout: View, message: String) {
        Snackbar.make(layout, message, Snackbar.LENGTH_SHORT).show()
    }

    fun makeSnackarLong(layout: View, message: String) {
        Snackbar.make(layout, message, Snackbar.LENGTH_LONG).show()
    }

    fun getAppViewModelProvider(): ViewModelProvider {
        return (applicationContext as App).getAppViewModelProvider(this)
    }

}