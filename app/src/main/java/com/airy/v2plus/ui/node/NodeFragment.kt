package com.airy.v2plus.ui.node

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airy.v2plus.databinding.NodeFragmentBinding
import com.airy.v2plus.showToastShort
import com.airy.v2plus.ui.base.BaseLazyFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class NodeFragment : BaseLazyFragment() {

    companion object {
        fun newInstance() = NodeFragment()
    }

    private val viewModel: NodeViewModel by viewModel()
    private lateinit var binding: NodeFragmentBinding
    private lateinit var nodesAdapter: NodesAdapter

    override fun setContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = NodeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun lazyLoad() {
        nodesAdapter = NodesAdapter(
            onClickCallback = {
                startActivity(Intent(requireActivity(), NodeActivity::class.java))
            },
            onLongClickCallback = { node ->
                node.title?.let {
                    requireContext().showToastShort(it)
                }
            })
        binding.list.apply {
            adapter = nodesAdapter
            setHasFixedSize(true)
        }

        binding.refresh.let {
            it.isRefreshing = true
            it.setOnRefreshListener {
                it.isRefreshing = true
            }
        }

        viewModel.nodes.observe(viewLifecycleOwner, {
            if (this::nodesAdapter.isInitialized) {
                nodesAdapter.submitList(it)
                binding.refresh.isRefreshing = false
            }
        })
    }

}
