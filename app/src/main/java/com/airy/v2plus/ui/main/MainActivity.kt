package com.airy.v2plus.ui.main

import android.content.Intent
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.airy.v2plus.*
import com.airy.v2plus.bean.custom.Balance
import com.airy.v2plus.databinding.ActivityMainBinding
import com.airy.v2plus.databinding.NavHeaderBinding
import com.airy.v2plus.event.RequestUserInfoFromLoginEvent
import com.airy.v2plus.network.RequestHelper
import com.airy.v2plus.remote.Broadcasts
import com.airy.v2plus.remote.ShortcutHelper
import com.airy.v2plus.service.RedeemService
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

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val TAG = "MainActivity"

    private val viewModel: MainViewModel by viewModels()
    private lateinit var contentBinding: ActivityMainBinding
    private lateinit var viewPage: ViewPager
    private lateinit var fragmentAdapter: FragmentViewPagerAdapter
    private lateinit var navigation: BottomNavigationView
    private var navHeaderBinding: NavHeaderBinding? = null
    private lateinit var nightModeSwitch: SwitchMaterial

    private val receiver = object : Broadcasts.Receiver {
        override fun onRedeemSuccess(balance: Balance?) {
            viewModel.isRedeemed.set(true)
            balance?.let {
                viewModel.balance.postValue(it)
            }
        }
    }

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
        ShortcutHelper.addRedeemShortcut()
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
        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            contentBinding.drawer,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
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
                    startRedeemService()
                }
            }

            if (UserCenter.getUserId() != 0L) {
                header.balanceLayout.visibility = View.VISIBLE
            }
        }

        // night switch
        nightModeSwitch =
            contentBinding.navigationView.menu.findItem(R.id.night_mode).actionView as SwitchMaterial
        nightModeSwitch.isChecked = this.isNightMode()
        nightModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                shareViewModel?.theme?.postValue(Theme.DARK)
            } else {
                shareViewModel?.theme?.postValue(Theme.LIGHT)
            }
            setDarkModeStorage(isChecked)
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
            when (it.itemId) {
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
        Broadcasts.register(receiver)
    }

    override fun loadData() {
        if (!RequestHelper.isExpired()) {
            ShortcutHelper.addRedeemShortcut()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Broadcasts.unregister(receiver)
        EventBus.getDefault().unregister(this)
    }

    private fun subscribeUI() {

        viewModel.user.observe(this, { u ->
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

        viewModel.balance.observe(this, { navHeaderBinding?.balance = it })

        viewModel.error.observe(this, { showToastLong(it.toString()) })

        viewModel.pageUserInfo.observe(this, {
            if (it.isEmpty()) {
                if (UserCenter.getUserId() != 0L && RequestHelper.isExpired()) {
                    showToastLong("As if your login status is expired, try to re-login~")
                }
            } else {
                autoRedeem()
            }
        })
    }

    private fun autoRedeem() {
        if (!viewModel.isRedeemed.get() && SharedPreferencesUtil.isAutoRedeem()) {
            showToastShort("Try to get your redeem now...")
            startRedeemService()
        }
    }

    private fun startRedeemService() {
        val intent = Intent(this, RedeemService::class.java)
        startService(intent)
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
