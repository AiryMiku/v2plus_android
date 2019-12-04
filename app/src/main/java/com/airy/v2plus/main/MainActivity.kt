package com.airy.v2plus.main

import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.widget.ViewPager2
import com.airy.v2plus.R
import com.airy.v2plus.base.BaseActivity
import com.airy.v2plus.databinding.ActivityMainBinding
import com.airy.v2plus.event.RequestUserInfoFromLoginEvent
import com.airy.v2plus.login.LoginActivity
import com.airy.v2plus.util.UserCenter
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import de.hdodenhof.circleimageview.CircleImageView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity :BaseActivity() {

    private val TAG = "MainActivity"

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var avatarImageView: CircleImageView
    private lateinit var userNameTextView: TextView
    private lateinit var viewPage: ViewPager2
    private lateinit var navigation: BottomNavigationView
    private lateinit var toolbar: Toolbar

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRequestUserInfoFromLoginEvent(e: RequestUserInfoFromLoginEvent) {
        viewModel.getUserInfoByName()
    }

    override fun setContentView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    override fun initViews() {
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        toolbar = findViewById(R.id.toolbar)
        val actionBarDrawerToggle = ActionBarDrawerToggle(this, binding.drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        binding.drawer.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        avatarImageView = binding.navigationView.getHeaderView(0).findViewById(R.id.avatar)
        userNameTextView = binding.navigationView.getHeaderView(0).findViewById(R.id.user_name)
        avatarImageView.setOnClickListener {
            navToActivity(this, LoginActivity::class.java)
        }
        userNameTextView.setOnClickListener {
            navToActivity(this, LoginActivity::class.java)
        }

        viewPage = binding.viewPager
        viewPage.adapter = FragmentViewPagerAdapter(this)
        viewPage.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when(position) {
                    0 -> {
                        toolbar.title = "Home"
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

        navigation = binding.bottomNavigation
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

    override fun loadData() {
        viewModel.getUserInfoById()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    private fun subscribeUI() {
        viewModel.user.observe(this, Observer { u->
            Glide.with(this).load("https:${u.avatarNormalUrl}").into(avatarImageView)
            userNameTextView.text = u.username
            avatarImageView.setOnClickListener {
                makeToastShort("Hello ${u.username}~")
            }
            if (UserCenter.getUserName().isNotBlank()) {
                UserCenter.setUserId(u.id.toString())
            }
        })
    }
}
