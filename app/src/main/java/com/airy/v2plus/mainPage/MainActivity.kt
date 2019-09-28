package com.airy.v2plus.mainPage

import android.content.Intent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import com.airy.v2plus.R
import com.airy.v2plus.base.BaseActivity
import com.airy.v2plus.databinding.ActivityMainBinding
import com.airy.v2plus.login.LoginActivity
import de.hdodenhof.circleimageview.CircleImageView

class MainActivity :BaseActivity() {

    private lateinit var mViewModel: MainPageViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var mAdapter: PageCellsAdapter
    private lateinit var avatarImageView: CircleImageView

    override fun setContentView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    override fun initViews() {
        mViewModel = ViewModelProviders.of(this).get(MainPageViewModel::class.java)
        mAdapter = PageCellsAdapter {}
        binding.list.adapter = mAdapter
        binding.list.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        binding.refresh.let {
            it.setOnRefreshListener {
                mViewModel.getMainPageData()
                it.isRefreshing = true
            }
        }

        avatarImageView = binding.navigationView.getHeaderView(0).findViewById(R.id.avatar)
        avatarImageView.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        mViewModel.mainPageList.observe(this, Observer {
            mAdapter.submitList(it)
            binding.refresh.isRefreshing = false
        })
    }

}
