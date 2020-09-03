package com.airy.v2plus.ui.settings

import android.os.Bundle
import android.widget.Toast
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.airy.v2plus.R
import com.airy.v2plus.ui.base.BaseActivity

class SettingsActivity : BaseActivity() {

    override fun setContentView() {
        setContentView(R.layout.settings_activity)
    }

    override val toolbarLabel: CharSequence? = "Settings"
    override var displayHomeAsUpEnabled: Boolean? = true

    override fun initViews() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            findPreference<SwitchPreferenceCompat>("auto_redeem")?.setOnPreferenceChangeListener { preference, newValue ->
                Toast.makeText(requireContext(), "value: $newValue", Toast.LENGTH_SHORT).show()
                true
            }
        }
    }
}