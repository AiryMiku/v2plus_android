package com.airy.v2plus.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import com.airy.v2plus.databinding.HomeFragmentBinding
import com.airy.v2plus.mainPage.PageCellsAdapter

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: HomeFragmentBinding
    private lateinit var mAdapter: PageCellsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        // TODO: Use the ViewModel

        mAdapter = PageCellsAdapter {}
        binding.list.adapter = mAdapter
        binding.list.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))

        binding.refresh.let {
            it.setOnRefreshListener {
                it.isRefreshing = true
                viewModel.getMainPageData()
            }
        }
    }

}
