package com.airy.v2plus.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.airy.v2plus.home.HomeFragment
import com.airy.v2plus.node.NodeFragment


/**
 * Created by Airy on 2019-10-20
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

class FragmentViewPagerAdapter(fa: FragmentActivity): FragmentStateAdapter(fa) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment.newInstance()
            1 -> HomeFragment.newInstance()
            2 -> NodeFragment.newInstance()
            else -> HomeFragment.newInstance()
        }
    }


}