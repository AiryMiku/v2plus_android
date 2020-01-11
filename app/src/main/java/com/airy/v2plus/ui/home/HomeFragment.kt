package com.airy.v2plus.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.airy.v2plus.Common
import com.airy.v2plus.databinding.HomeFragmentBinding
import com.airy.v2plus.ui.main.MainViewModel
import com.airy.v2plus.ui.topic.TopicDetailActivity

class HomeFragment: Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val TAG = "HomeFragment"

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: HomeFragmentBinding
    private lateinit var adapter: MainPageItemsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this.activity!!).get(MainViewModel::class.java)
        adapter = MainPageItemsAdapter(activity) {
            val intent = Intent(activity, TopicDetailActivity::class.java)
            intent.putExtra(Common.KEY_ID.TOPIC, it.topicId)
            startActivity(intent)
        }
        binding.list.adapter = adapter

        viewModel.getMainPageResponse()
        binding.refresh.let {
            it.isRefreshing = true
            it.setOnRefreshListener {
                viewModel.getMainPageResponse()
                binding.refresh.isRefreshing = true
            }
        }

        subscribeUI()
    }

    private fun subscribeUI() {
        viewModel.mainListItem.observe(this, Observer {
            adapter.submitList(it)
            binding.refresh.isRefreshing = false
        })
        viewModel.error.observe(this, Observer {
            binding.refresh.isRefreshing = false
        })
    }

}
