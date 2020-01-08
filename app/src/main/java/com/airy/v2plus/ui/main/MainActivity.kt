package com.airy.v2plus.ui.main

import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.airy.v2plus.R
import com.airy.v2plus.databinding.ActivityMainBinding
import com.airy.v2plus.databinding.NavHeaderBinding
import com.airy.v2plus.event.RequestUserInfoFromLoginEvent
import com.airy.v2plus.ui.base.BaseActivity
import com.airy.v2plus.ui.login.LoginActivity
import com.airy.v2plus.ui.settings.SettingsActivity
import com.airy.v2plus.util.UserCenter
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.switchmaterial.SwitchMaterial
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity :BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val TAG = "MainActivity"

    private lateinit var viewModel: MainViewModel
    private lateinit var contentBinding: ActivityMainBinding
    private lateinit var viewPage: ViewPager
    private lateinit var navigation: BottomNavigationView
    private lateinit var toolbar: Toolbar
    private var navHeaderBinding: NavHeaderBinding? = null
    private lateinit var nightModeSwitch: SwitchMaterial

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRequestUserInfoFromLoginEvent(e: RequestUserInfoFromLoginEvent) {
        viewModel.getUserInfoByName()
    }


    override fun setContentView() {
        contentBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    override fun initViews() {
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        // action bar
        toolbar = findViewById(R.id.toolbar)
        val actionBarDrawerToggle = ActionBarDrawerToggle(this, contentBinding.drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        contentBinding.drawer.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        // nav head
        contentBinding.navigationView.getHeaderView(0).run {
            DataBindingUtil.bind<NavHeaderBinding>(this)
            navHeaderBinding = DataBindingUtil.getBinding(this)
        }
        navHeaderBinding?.let { header->
            header.avatar.setOnClickListener {
                navToActivity(this, LoginActivity::class.java)
            }
            header.userName.setOnClickListener {
                navToActivity(this, LoginActivity::class.java)
            }
            header.redeem.setOnClickListener {
//                makeToastShort("Processing...")
                header.redeem.text = getString(R.string.working)
                viewModel.getDailyMissionRedeem()
            }
        }

        contentBinding.navigationView.setNavigationItemSelectedListener(this)

        // night switch
        nightModeSwitch = contentBinding.navigationView.menu.findItem(R.id.night_mode).actionView as SwitchMaterial
        nightModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
                makeToastShort("night mode on")
            } else {
                delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
                makeToastShort("night mode off")
            }
        }

        // view page
        viewPage = contentBinding.viewPager
        viewPage.adapter = FragmentViewPagerAdapter(supportFragmentManager)
        viewPage.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {

                        navigation.selectedItemId = R.id.Home
                    }
                    1 -> {
                        toolbar.title = "Message"
                        navigation.selectedItemId = R.id.Message
                    }
                    2 -> {
                        toolbar.title = "Node"
                        navigation.selectedItemId = R.id.Node
                    }
                }
            }
        })
        toolbar.title = "Home"

        // bottom navigation view
        navigation = contentBinding.bottomNavigation
//        navigation.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED
        navigation.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.Home -> {
                    toolbar.title = "Home"
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

    override fun loadData() {
        viewModel.getUserInfoById()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    private fun subscribeUI() {
        viewModel.user.observe(this, Observer { u->
            navHeaderBinding?.let {
                Glide.with(this).load("https:${u.avatarNormalUrl}").into(it.avatar)
                it.userName.text = u.username
                it.avatar.setOnClickListener {
                    makeToastShort("Hello ${u.username}~")
                }
                if (UserCenter.getUserName().isNotBlank()) {
                    UserCenter.setUserId(u.id)
                }
            }
        })
        viewModel.balance.observe(this, Observer {
            navHeaderBinding?.balance = it
        })
        viewModel.redeemMessages.observe(this, Observer {
            navHeaderBinding?.redeem?.text = getString(R.string.redeem_done)
            makeToastLong(it.toString())
        })
        viewModel.error.observe(this, Observer {
            makeToastLong(it.toString())
        })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.hottest -> {
                makeToastShort("Developing~")
            }
            R.id.settings -> {
                navToActivity(this, SettingsActivity::class.java)
            }
            R.id.about -> {
                makeToastShort("Developing~")
            }
        }
        return true
    }
}
