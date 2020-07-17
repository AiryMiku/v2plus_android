package com.airy.v2plus.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.airy.v2plus.databinding.HomeFragmentBinding
import com.airy.v2plus.navToTopicActivity
import com.airy.v2plus.ui.base.BaseFragment
import com.airy.v2plus.ui.main.MainViewModel

class HomeFragment: BaseFragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val TAG = "HomeFragment"

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: HomeFragmentBinding
    private lateinit var adapter: MainPageItemsAdapter

    override fun initPrepare() {
        Log.d(TAG, "initPrepare")

        adapter = MainPageItemsAdapter(requireContext()) { item, holder ->
            navToTopicActivity(item.topicId)
            // Todo Add TransitionAnimation, need pass the avatar bitmap to topic activity
//            ActivityOptions.makeSceneTransitionAnimation(activity , holder.binding.avatar, "avatarView").toBundle()
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
        viewModel.mainListItem.observe(viewLifecycleOwner, Observer {
            binding.refresh.isRefreshing = false
            adapter.submitList(it)
        })
        viewModel.error.observe(viewLifecycleOwner, Observer {
            binding.refresh.isRefreshing = false
        })
    }
}
