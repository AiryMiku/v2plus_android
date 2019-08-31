package com.airy.v2plus.mainPage

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import com.airy.v2plus.R
import com.airy.v2plus.base.BaseActivity
import com.airy.v2plus.databinding.ActivityMainBinding

class MainActivity :BaseActivity() {

    private lateinit var mViewModel: MainPageViewModel
    private lateinit var mDataBinding: ActivityMainBinding
    private lateinit var mAdapter: PageCellsAdapter

    override fun setContentView() {
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel = ViewModelProviders.of(this).get(MainPageViewModel::class.java)
        mAdapter = PageCellsAdapter {}
        mDataBinding.list.adapter = mAdapter
        mDataBinding.list.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        mViewModel.mainPageList.observe(this, Observer {
            mAdapter.submitList(it)
        })
    }

}
