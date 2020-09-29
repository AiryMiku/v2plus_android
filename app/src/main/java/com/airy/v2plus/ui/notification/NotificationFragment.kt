package com.airy.v2plus.ui.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.airy.v2plus.databinding.FragmentNotificationsBinding
import com.airy.v2plus.navToTopicActivity
import com.airy.v2plus.repository.util.NetworkState
import com.airy.v2plus.base.BaseLazyFragment

class NotificationFragment : BaseLazyFragment() {

    companion object {
        fun newInstance() = NotificationFragment()
    }

    private val viewModel: NotificationViewModel by viewModels()
    private lateinit var binding: FragmentNotificationsBinding
    private lateinit var adapter: NotificationsAdapter

    override fun setContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun lazyLoad() {

        binding.vm = viewModel

        adapter = NotificationsAdapter {
            navToTopicActivity(it.topicId, if (it.isReply) it.replyNo else null)
        }
        binding.list.adapter = adapter
        binding.refresh.setOnRefreshListener {
            viewModel.refresh()
        }
        viewModel.networkState.observe(viewLifecycleOwner) {
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
        }

        viewModel.notifications.observe(viewLifecycleOwner) {
            if (this::adapter.isInitialized) {
                binding.refresh.isRefreshing = false
                adapter.submitList(it)
            }
        }

        viewModel.error.observe(viewLifecycleOwner) {
            binding.refresh.isRefreshing = false
            Toast.makeText(this.context, it.message, Toast.LENGTH_LONG).show()
        }
    }

}
