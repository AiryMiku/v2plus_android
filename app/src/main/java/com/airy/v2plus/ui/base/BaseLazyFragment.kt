package com.airy.v2plus.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


// only affected by using androidx.fragment.app.FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
abstract class BaseLazyFragment: BaseFragment() {

    protected var isInit = false
    protected var isLoaded = false

    private var rootView: View? = null

    abstract fun setContentView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = setContentView(inflater, container, savedInstanceState)
        isInit = true
        return rootView
    }

    override fun onResume() {
        super.onResume()
        if (isInit && !isLoaded && !isHidden) {
            lazyLoad()
            isLoaded = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isLoaded = false
    }

    open fun lazyLoad() {}

}