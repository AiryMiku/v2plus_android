package com.airy.v2plus.ui.main

import android.content.Intent
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.airy.v2plus.*
import com.airy.v2plus.databinding.ActivityMainBinding
import com.airy.v2plus.databinding.NavHeaderBinding
import com.airy.v2plus.event.RequestUserInfoFromLoginEvent
import com.airy.v2plus.ui.base.BaseActivity
import com.airy.v2plus.ui.home.HomeFragment
import com.airy.v2plus.ui.hot_or_latest.HotOrLatestActivity
import com.airy.v2plus.ui.login.LoginActivity
import com.airy.v2plus.ui.node.NodeFragment
import com.airy.v2plus.ui.notification.NotificationFragment
import com.airy.v2plus.ui.settings.SettingsActivity
import com.airy.v2plus.ui.theme.Theme
import com.airy.v2plus.util.SharedPreferencesUtil
import com.airy.v2plus.util.UserCenter
import com.airy.v2plus.util.setDarkModeStorage
import com.airy.v2plus.widget.ZoomOutPageTransformer
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.switchmaterial.SwitchMaterial
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity :BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val TAG = "MainActivity"

    private val viewModel: MainViewModel by viewModels()
    private lateinit var contentBinding: ActivityMainBinding
    private lateinit var viewPage: ViewPager
    private lateinit var fragmentAdapter: FragmentViewPagerAdapter
    private lateinit var navigation: BottomNavigationView
    private var navHeaderBinding: NavHeaderBinding? = null
    private lateinit var nightModeSwitch: SwitchMaterial

    private lateinit var fragmentList: MutableList<Fragment>
    private lateinit var titleList: MutableList<String>
    private lateinit var iconIdList: MutableList<Int>

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRequestUserInfoFromLoginEvent(e: RequestUserInfoFromLoginEvent) {
        viewModel.getUserInfoByName()
        viewModel.getMainPageResponse()
        navHeaderBinding?.let { header ->
            header.balanceLayout.visibility = View.VISIBLE
        }
    }

    override val toolbarLabel: CharSequence? = "Home"
    override var displayHomeAsUpEnabled: Boolean? = null

    override fun setContentView() {
        contentBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    override fun initViews() {
        // init listener
        contentBinding.navigationView.setNavigationItemSelectedListener(this)

        // action bar
        val actionBarDrawerToggle = ActionBarDrawerToggle(this, contentBinding.drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        contentBinding.drawer.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        // nav head
        contentBinding.navigationView.getHeaderView(0).run {
            DataBindingUtil.bind<NavHeaderBinding>(this)
            navHeaderBinding = DataBindingUtil.getBinding(this)
        }

        navHeaderBinding?.let { header ->

            header.vm = viewModel

            header.avatar.setOnClickListener {
                navToActivity(this, LoginActivity::class.java)
            }

            if (!UserCenter.getUserName().isBlank()) {
                header.userName.text = UserCenter.getUserName()
            }

            header.userName.setOnClickListener {
                navToActivity(this, LoginActivity::class.java)
            }

            header.redeem.setOnClickListener {
                if (viewModel.isRedeemed.get()) {
                    showToastShort("You has been redeemed, Congratulation~")
                } else {
                    header.redeem.text = getString(R.string.working)
                    viewModel.getDailyMissionRedeem()
                }
            }

            if (UserCenter.getUserId() != 0L) {
                header.balanceLayout.visibility = View.VISIBLE
            }
        }

        // night switch
        nightModeSwitch = contentBinding.navigationView.menu.findItem(R.id.night_mode).actionView as SwitchMaterial
        nightModeSwitch.isChecked = this.isNightMode()
        nightModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                shareViewModel?.theme?.postValue(Theme.DARK)
            } else {
                shareViewModel?.theme?.postValue(Theme.LIGHT)
            }
            setDarkModeStorage(isChecked)
            Log.d(TAG, "now app theme mode ${delegate.localNightMode}")
        }

        // init fragments
        fragmentList = mutableListOf<Fragment>().apply {
            add(HomeFragment.newInstance())
            add(NotificationFragment.newInstance())
            add(NodeFragment.newInstance())
        }
        titleList = mutableListOf("Home", "Message", "Node")
        iconIdList = mutableListOf(R.id.Home, R.id.Message, R.id.Node)

        // view page
        viewPage = contentBinding.viewPager
        viewPage.offscreenPageLimit = fragmentList.size - 1
        fragmentAdapter = FragmentViewPagerAdapter(fragmentList, titleList, supportFragmentManager)
        viewPage.adapter = fragmentAdapter
        viewPage.setPageTransformer(true, ZoomOutPageTransformer())
        viewPage.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                toolbar?.title = fragmentAdapter.getPageTitle(position)
                navigation.selectedItemId = iconIdList[position]
            }
        })

        // bottom navigation view
        navigation = contentBinding.bottomNavigation
        // navigation.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED
        navigation.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.Home -> {
                    viewPage.currentItem = 0
                    true
                }
                R.id.Message -> {
                    viewPage.currentItem = 1
                    true
                }
                R.id.Node -> {
                    viewPage.currentItem = 2
                    true
                }
                else -> false
            }
        }

        subscribeUI()

        EventBus.getDefault().register(this)
    }

    override fun loadData() { }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    private fun subscribeUI() {

        viewModel.user.observe(this, Observer { u->
            navHeaderBinding?.let {
                Glide.with(this).load(u.avatarNormalUrl).into(it.avatar)
                it.userName.text = u.username
                it.avatar.setOnClickListener {
                    showToastShort("Hello ${u.username}~")
                }
                if (UserCenter.getUserName().isNotBlank()) {
                    UserCenter.setUserId(u.id)
                }
            }
        })

        viewModel.balance.observe(this, Observer { navHeaderBinding?.balance = it })

        viewModel.redeemMessages.observe(this, Observer { event ->
            if (event != null) {
                event.getContentIfNotHandled()?.let {
                    showToastLong(it.toString())
                }
            }
        })

        viewModel.error.observe(this, Observer { showToastLong(it.toString()) })

        viewModel.pageUserInfo.observe(this, Observer {
            if (it.isEmpty()) {
                if (UserCenter.getUserId() != 0L) {
                    showToastLong("As if your login status is expired, try to re-login~")
                }
            } else if (!viewModel.isRedeemed.get() && SharedPreferencesUtil.isAutoRedeem()) {
                showToastShort("Try to get your redeem now...")
                viewModel.getDailyMissionRedeem()
            }
        })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.latest -> {
                val i = Intent(this, HotOrLatestActivity::class.java)
                i.putExtra(Common.KEY_BOOLEAN.IS_LATEST, true)
                startActivity(i)
            }
            R.id.hottest -> {
                navToActivity(this, HotOrLatestActivity::class.java)
            }
            R.id.settings -> {
                navToActivity(this, SettingsActivity::class.java)
            }
            R.id.about -> {
                showToastShort("Developing~")
            }
        }
        return true
    }
}
