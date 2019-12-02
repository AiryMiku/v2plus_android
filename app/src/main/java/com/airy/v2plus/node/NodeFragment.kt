package com.airy.v2plus.node

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.airy.v2plus.databinding.NodeFragmentBinding

class NodeFragment : Fragment() {

    companion object {
        fun newInstance() = NodeFragment()
    }

    private lateinit var viewModel: NodeViewModel
    private lateinit var binding: NodeFragmentBinding
    private lateinit var adapter: NodesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = NodeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(NodeViewModel::class.java)
        adapter = NodesAdapter {  }
        binding.list.adapter = adapter

        binding.refresh.let {
            it.isRefreshing = true
            it.setOnRefreshListener {
                viewModel.getAllNode()
                it.isRefreshing = true
            }
        }
        viewModel.getAllNode()

        viewModel.nodes.observe(this, Observer {
            adapter.submitList(it)
            binding.refresh.isRefreshing = false
        })
    }

}
