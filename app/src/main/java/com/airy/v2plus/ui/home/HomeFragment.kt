package com.airy.v2plus.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.airy.v2plus.Common
import com.airy.v2plus.databinding.HomeFragmentBinding
import com.airy.v2plus.ui.base.BaseFragment
import com.airy.v2plus.ui.main.MainViewModel
import com.airy.v2plus.ui.topic.TopicDetailActivity

class HomeFragment: BaseFragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val TAG = "HomeFragment"

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: HomeFragmentBinding
    private lateinit var adapter: MainPageItemsAdapter

    override fun initPrepare() {
        Log.d(TAG, "initPrepare")
        viewModel = ViewModelProviders.of(this.activity!!).get(MainViewModel::class.java)

        adapter = MainPageItemsAdapter(activity) { item, holder ->
            val intent = Intent(activity, TopicDetailActivity::class.java)
            intent.putExtra(Common.KEY_ID.TOPIC, item.topicId)
            startActivity(intent)
            // Todo Add TransitionAnimation
            // ActivityOptions.makeSceneTransitionAnimation(activity , holder.binding.avatar, "avatarView").toBundle()
        }
        binding.list.adapter = adapter

        binding.refresh.let {
            it.isRefreshing = true
            it.setOnRefreshListener {
                viewModel.getMainPageResponse()
                binding.refresh.isRefreshing = true
            }
        }

        subscribeUI()
    }

    override fun initData() {
        Log.d(TAG, "initData")
    }

    override fun initView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun subscribeUI() {
        viewModel.mainListItem.observe(this, Observer {
            binding.refresh.isRefreshing = false
            adapter.submitList(it)
        })
        viewModel.error.observe(this, Observer {
            binding.refresh.isRefreshing = false
        })
    }
}
