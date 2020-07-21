package com.airy.v2plus.ui.node

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.airy.v2plus.databinding.NodeFragmentBinding
import com.airy.v2plus.ui.base.BaseLazyFragment

class NodeFragment : BaseLazyFragment() {

    companion object {
        fun newInstance() = NodeFragment()
    }

    private val viewModel: NodeViewModel by viewModels()
    private lateinit var binding: NodeFragmentBinding
    private lateinit var adapter: NodesAdapter

    override fun setContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = NodeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun lazyLoad() {
        adapter = NodesAdapter()
        binding.list.adapter = adapter

        binding.refresh.let {
            it.isRefreshing = true
            it.setOnRefreshListener {
                viewModel.getAllNode()
                it.isRefreshing = true
            }
        }
        viewModel.getAllNode()

        viewModel.nodes.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            binding.refresh.isRefreshing = false
        })
    }

}
