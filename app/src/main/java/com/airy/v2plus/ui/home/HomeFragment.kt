package com.airy.v2plus.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.airy.v2plus.Config
import com.airy.v2plus.databinding.FragmentHomeBinding
import com.airy.v2plus.navToTopicActivity
import com.airy.v2plus.base.BaseLazyFragment
import com.airy.v2plus.ui.main.MainViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.ListPreloader
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader
import com.bumptech.glide.util.ViewPreloadSizeProvider
import java.util.*

class HomeFragment: BaseLazyFragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val TAG = "HomeFragment"

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: MainPageItemsAdapter
    private lateinit var preLoadSizeProvider: ViewPreloadSizeProvider<String>
    private lateinit var preLoadModelProvider: ListPreloader.PreloadModelProvider<String>
    private lateinit var preLoader: RecyclerViewPreloader<String>

    override fun setContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun lazyLoad() {
        preLoadSizeProvider = ViewPreloadSizeProvider<String>()
        preLoadModelProvider = object: ListPreloader.PreloadModelProvider<String> {
            override fun getPreloadItems(position: Int): MutableList<String> {
                val url: String? = viewModel.mainListItem.value?.getOrNull(position)?.avatarUrl
                return if (url == null) {
                    emptyList<String>().toMutableList()
                } else {
                    Collections.singletonList(url)
                }
            }

            override fun getPreloadRequestBuilder(item: String): RequestBuilder<*>? {
                return Glide.with(requireContext()).load(item).override(Config.ViewSize.AVATAR)
            }
        }
        preLoader = RecyclerViewPreloader(Glide.with(requireContext()), preLoadModelProvider, preLoadSizeProvider, 8)
        binding.list.addOnScrollListener(preLoader)
        adapter = MainPageItemsAdapter() { item, _ ->
            navToTopicActivity(item.topicId)
            // Todo Add TransitionAnimation, need pass the avatar bitmap to topic activity
            // ActivityOptions.makeSceneTransitionAnimation(activity , holder.binding.avatar, "avatarView").toBundle()
        }
        binding.list.adapter = adapter

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
        viewModel.mainListItem.observe(viewLifecycleOwner) {
            binding.refresh.isRefreshing = false
            adapter.submitList(it)
        }
        viewModel.error.observe(viewLifecycleOwner) {
            binding.refresh.isRefreshing = false
        }
    }

}
