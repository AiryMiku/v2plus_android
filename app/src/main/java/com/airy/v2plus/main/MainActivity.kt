package com.airy.v2plus.main

import android.content.Intent
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

    private lateinit var viewModel: MainPageViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var avatarImageView: CircleImageView
    private lateinit var viewPage: ViewPager2
    private lateinit var navigation: BottomNavigationView
    private lateinit var toolbar: Toolbar

    override fun setContentView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    override fun initViews() {
        viewModel = ViewModelProviders.of(this).get(MainPageViewModel::class.java)


        toolbar = findViewById(R.id.toolbar)
        val actionBarDrawerToggle = ActionBarDrawerToggle(this, binding.drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        binding.drawer.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        avatarImageView = binding.navigationView.getHeaderView(0).findViewById(R.id.avatar)
        avatarImageView.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        viewPage = binding.viewPager
        viewPage.adapter = FragmentViewPagerAdapter(this)
        viewPage.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when(position) {
                    0 -> toolbar.title = "Home"
                    1 -> toolbar.title = "Message"
                    2 -> toolbar.title = "Node"
                }
            }
        })

        navigation = binding.bottomNavigation
        navigation.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.Home -> {
                    viewPage.currentItem = 0
                    toolbar.title = "Home"
                    true
                }
                R.id.Message -> {
                    viewPage.currentItem = 1
                    toolbar.title = "Message"
                    true
                }
                R.id.Node -> {
                    viewPage.currentItem = 2
                    toolbar.title = "Node"
                    true
                }
                else -> false
            }
        }


    }


}
