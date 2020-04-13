package com.airy.v2plus.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment


abstract class BaseFragment: Fragment() {

    private var mContext: Context? = null
    private var mRootView: View? = null
    private var mPrepared: Boolean = false
    private var mFirstInit: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = activity
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mPrepared = true
        initPrepare()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (mRootView == null) {
            mRootView = initView(inflater, container, savedInstanceState)
        }
        return mRootView
    }

    override fun onResume() {
        super.onResume()
        if (isVisible) {
            lazyLoad()
        } else {
            onInvisible()
        }
    }

    protected open fun lazyLoad() {
        if (!mPrepared || !isVisible || !mFirstInit) {
            // mPrepared 父activity
            // isVisible 可见性
            // mFirstInit 切换加载判断
            return
        }
        initData()
        mFirstInit = false
    }
    /**
     * 在onActivityCreated中调用的方法，可以用来进行初始化操作。
     */
    protected abstract fun initPrepare()

    /**
     * fragment被设置为不可见时调用
     */
    protected open fun onInvisible() {}

    /**
     * 这里获取数据，刷新界面
     */
    protected abstract fun initData()

    /**
     * 初始化布局，请不要把耗时操作放在这个方法里，这个方法用来提供一个
     * 基本的布局而非一个完整的布局，以免ViewPager预加载消耗大量的资源。
     */
    protected abstract fun initView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View


}