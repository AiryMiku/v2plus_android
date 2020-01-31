package com.airy.v2plus

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
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
    private lateinit var factory: ViewModelProvider.Factory

    override fun onCreate() {
        super.onCreate()
        instance = this

        viewModelStore = ViewModelStore()
    }

    override fun getViewModelStore(): ViewModelStore {
        return viewModelStore
    }

    fun getAppViewModelProvider(activity: Activity): ViewModelProvider {
        return ViewModelProvider(
            (activity.applicationContext as App),
            (activity.applicationContext as App).getAppFactory(activity)
        )
    }

    private fun getAppFactory(activity: Activity): ViewModelProvider.Factory {
        val application = checkApplication(activity)!!
        if (this::factory.isInitialized) {
            factory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        }
        return factory
    }

    private fun checkApplication(activity: Activity): Application? {
        return activity.application
            ?: throw IllegalStateException(
                "Your activity/fragment is not yet attached to "
                        + "Application. You can't request ViewModel before onCreate call."
            )
    }

}