package com.airy.v2plus.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.airy.v2plus.home.HomeFragment
import com.airy.v2plus.message.MessageFragment
import com.airy.v2plus.node.NodeFragment


/**
 * Created by Airy on 2019-10-20
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

class FragmentViewPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment.newInstance()
            1 -> MessageFragment.newInstance()
            2 -> NodeFragment.newInstance()
            else -> HomeFragment.newInstance()
        }
    }

    override fun getCount(): Int {
        return 3
    }


}