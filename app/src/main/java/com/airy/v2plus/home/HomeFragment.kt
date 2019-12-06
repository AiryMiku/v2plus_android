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
import com.airy.v2plus.main.MainViewModel

class HomeFragment: Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val TAG = "HomeFragment"

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: HomeFragmentBinding
    private lateinit var adapter: CellItemsAdapter

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
        adapter = CellItemsAdapter(activity)
        binding.list.adapter = adapter
        binding.list.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))

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
    }

}
