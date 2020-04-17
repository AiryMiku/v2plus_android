package com.airy.v2plus.ui.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.airy.v2plus.R
import com.airy.v2plus.ui.base.BaseActivity

class SettingsActivity : BaseActivity() {

    override fun setContentView() {
        setContentView(R.layout.settings_activity)
    }

    override val toolbarLabel: CharSequence? = "Settings"

    override fun initViews() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }
    }
}