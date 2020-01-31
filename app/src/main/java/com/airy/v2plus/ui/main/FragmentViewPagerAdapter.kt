package com.airy.v2plus.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.airy.v2plus.ui.home.HomeFragment
import com.airy.v2plus.ui.notification.NotificationFragment
import com.airy.v2plus.ui.node.NodeFragment


/**
 * Created by Airy on 2019-10-20
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

class FragmentViewPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragments = arrayOf(HomeFragment.newInstance(), NotificationFragment.newInstance(), NodeFragment.newInstance())

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }
}