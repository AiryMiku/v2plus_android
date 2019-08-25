package com.airy.v2plus.mainPage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import com.airy.v2plus.R
import com.airy.v2plus.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainPageViewModel
    private lateinit var dataBinding: ActivityMainBinding
    private lateinit var adapter: PageCellsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(MainPageViewModel::class.java)
        adapter = PageCellsAdapter {}
        dataBinding.list.adapter = adapter
        dataBinding.list.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        viewModel.mainPageList.observe(this, Observer {
            adapter.submitList(it)
        })
    }
}
