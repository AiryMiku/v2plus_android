package com.airy.v2plus.ui.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.airy.v2plus.databinding.NotificationFragmentBinding
import com.airy.v2plus.navToTopicActivity
import com.airy.v2plus.repository.util.NetworkState
import com.airy.v2plus.ui.base.BaseLazyFragment

class NotificationFragment : BaseLazyFragment() {

    companion object {
        fun newInstance() = NotificationFragment()
    }

    private val viewModel: NotificationViewModel by viewModels()
    private lateinit var binding: NotificationFragmentBinding
    private lateinit var adapter: NotificationsAdapter

//    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
//    fun onRequestUserHadLoginEvent(e: RequestUserHadLoginEvent) {
//        if (this::adapter.isInitialized) {
//            adapter = NotificationPagedListAdapter(requireContext()) {
//                navToTopicActivity(it.topicId, if (it.isReply) it.replyNo else null)
//            }
//            binding.list.adapter = adapter
//        }
//    }

    override fun setContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = NotificationFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun lazyLoad() {

        binding.vm = viewModel

        adapter = NotificationsAdapter(requireContext()) {
            navToTopicActivity(it.topicId, if (it.isReply) it.replyNo else null)
        }
        binding.list.adapter = adapter
        binding.refresh.setOnRefreshListener {
            viewModel.refresh()
        }
        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            when(it) {
                NetworkState.LOADING -> {
                    binding.refresh.isRefreshing = true
                }
                NetworkState.LOADED -> {
                    binding.refresh.isRefreshing = false
                }
                else -> {
                    binding.refresh.isRefreshing = false
                }
            }
        })

        viewModel.notifications.observe(viewLifecycleOwner, Observer {
            if (this::adapter.isInitialized) {
                binding.refresh.isRefreshing = false
                adapter.submitList(it)
            }
        })

        viewModel.error.observe(viewLifecycleOwner, Observer {
            binding.refresh.isRefreshing = false
            Toast.makeText(this.context, it.message, Toast.LENGTH_LONG).show()
        })
    }

}
