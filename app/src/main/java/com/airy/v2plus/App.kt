package com.airy.v2plus

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.airy.v2plus.remote.Broadcasts
import com.airy.v2plus.remote.ShortcutHelper
import com.airy.v2plus.ui.share.ShareViewModel
import com.airy.v2plus.util.getDarkModeStorage
import kotlin.properties.Delegates


/**
 * Created by Airy on 2019-09-16
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

class App: Application(), ViewModelStoreOwner {

    companion object {
        private var instance: App by Delegates.notNull()

        fun getAppContext(): Context = instance
    }

    private lateinit var viewModelStore: ViewModelStore
    private var factory: ViewModelProvider.Factory? = null

    override fun onCreate() {
        super.onCreate()
        instance = this

        viewModelStore = ViewModelStore()

        if (getDarkModeStorage()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        Broadcasts.init(this)
        ShortcutHelper.init(this)
    }

    override fun getViewModelStore(): ViewModelStore {
        return viewModelStore
    }

    fun getAppViewModelProvider(activity: Activity): ViewModelProvider? {
        return (activity.applicationContext as App).getAppFactory(activity)?.let {
            ViewModelProvider((activity.applicationContext as App), it)
        }
    }

    private fun getAppFactory(activity: Activity): ViewModelProvider.Factory? {
        val application = checkApplication(activity)
        application?.let {
            factory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        }
        return factory
    }

    private fun checkApplication(activity: Activity): Application? {
        return activity.application
            ?: throw IllegalStateException(
                "Your activity/fragment is not yet attached to Application. You can't request ViewModel before onCreate call."
            )
    }

    private fun checkActivity(fragment: Fragment): Activity? {
        return fragment.activity ?: throw IllegalStateException("Can't create ViewModelProvider for detached fragment")
    }

}