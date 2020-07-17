package com.airy.v2plus.ui.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.airy.v2plus.databinding.NotificationFragmentBinding
import com.airy.v2plus.event.RequestUserHadLoginEvent
import com.airy.v2plus.event.RequestUserInfoFromLoginEvent
import com.airy.v2plus.navToTopicActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class NotificationFragment : Fragment() {

    companion object {
        fun newInstance() = NotificationFragment()
    }

    private val viewModel: NotificationViewModel by viewModels()
    private lateinit var binding: NotificationFragmentBinding
//    private lateinit var adapter: NotificationsAdapter
    private lateinit var adapter: NotificationPagedListAdapter

//    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
//    fun onRequestUserHadLoginEvent(e: RequestUserHadLoginEvent) {
//        if (this::adapter.isInitialized) {
//            adapter = NotificationPagedListAdapter(requireContext()) {
//                navToTopicActivity(it.topicId, if (it.isReply) it.replyNo else null)
//            }
//            binding.list.adapter = adapter
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = NotificationFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // should init adapter
        adapter = NotificationPagedListAdapter(requireContext()) {
            navToTopicActivity(it.topicId, if (it.isReply) it.replyNo else null)
        }
        binding.list.adapter = adapter

//        adapter = NotificationsAdapter(this.context)
//        viewModel.getNotification(1)
//        binding.refresh.isRefreshing = true
//        viewModel.notificationPage.observe(this, Observer {
//            binding.refresh.isRefreshing = false
//            adapter.submitList(it.items)
//        })

        viewModel.pagedData.observe(viewLifecycleOwner, Observer {
            if (this::adapter.isInitialized) {
                adapter.submitList(it)
            }
        })

        viewModel.error.observe(viewLifecycleOwner, Observer {
            binding.refresh.isRefreshing = false
            Toast.makeText(this.context, it.message, Toast.LENGTH_LONG).show()
        })
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}
