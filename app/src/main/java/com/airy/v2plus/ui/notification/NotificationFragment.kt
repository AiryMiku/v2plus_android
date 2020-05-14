package com.airy.v2plus.ui.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.airy.v2plus.databinding.NotificationFragmentBinding
import com.airy.v2plus.navToTopicActivity

class NotificationFragment : Fragment() {

    companion object {
        fun newInstance() = NotificationFragment()
    }

    private lateinit var viewModel: NotificationViewModel
    private lateinit var binding: NotificationFragmentBinding
//    private lateinit var adapter: NotificationsAdapter
    private lateinit var adapter: NotificationPagedListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = NotificationFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(NotificationViewModel::class.java)
//        adapter = NotificationsAdapter(this.context)
        adapter = NotificationPagedListAdapter() {
            navToTopicActivity(it.topicId, it.replyNo)
        }
        binding.list.adapter = adapter
//        viewModel.getNotification(1)
//        binding.refresh.isRefreshing = true

//        viewModel.notificationPage.observe(this, Observer {
//            binding.refresh.isRefreshing = false
//            adapter.submitList(it.items)
//        })

        viewModel.pagedData.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        viewModel.error.observe(viewLifecycleOwner, Observer {
            binding.refresh.isRefreshing = false
            Toast.makeText(this.context, it.message, Toast.LENGTH_LONG).show()
        })
    }

}
