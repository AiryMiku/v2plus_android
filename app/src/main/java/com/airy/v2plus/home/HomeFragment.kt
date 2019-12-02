package com.airy.v2plus.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import com.airy.v2plus.databinding.HomeFragmentBinding
import com.airy.v2plus.main.CellItemsAdapter
import com.airy.v2plus.main.MainViewModel

class HomeFragment: Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val TAG = "HomeFragment"

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: HomeFragmentBinding
    private lateinit var mAdapter: CellItemsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        mAdapter = CellItemsAdapter(activity)
        binding.list.adapter = mAdapter
        binding.list.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))

        viewModel.getMainPageList()
        binding.refresh.let {
            it.isRefreshing = true
            it.setOnRefreshListener {
                viewModel.getMainPageList()
                binding.refresh.isRefreshing = true
            }
        }

        subscribeUI()
    }

    private fun subscribeUI() {
        viewModel.mainListItem.observe(this, Observer {
            mAdapter.submitList(it)
            binding.refresh.isRefreshing = false
        })
    }

}
