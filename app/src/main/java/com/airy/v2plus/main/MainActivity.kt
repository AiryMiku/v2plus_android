package com.airy.v2plus.main

import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.widget.ViewPager2
import com.airy.v2plus.R
import com.airy.v2plus.base.BaseActivity
import com.airy.v2plus.databinding.ActivityMainBinding
import com.airy.v2plus.login.LoginActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import de.hdodenhof.circleimageview.CircleImageView

class MainActivity :BaseActivity() {

    private val TAG = "MainActivity"

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var avatarImageView: CircleImageView
    private lateinit var viewPage: ViewPager2
    private lateinit var navigation: BottomNavigationView
    private lateinit var toolbar: Toolbar

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
        avatarImageView.setOnClickListener {
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

    }
}
