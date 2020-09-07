package com.airy.v2plus.ui.settings

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.airy.v2plus.R
import com.airy.v2plus.event.LogoutEvent
import com.airy.v2plus.network.RequestHelper
import com.airy.v2plus.ui.base.BaseActivity
import com.airy.v2plus.ui.login.LoginActivity
import com.airy.v2plus.util.UserCenter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.greenrobot.eventbus.EventBus

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
            findPreference<Preference>("log")?.run {
                setOnPreferenceClickListener {
                    if (RequestHelper.isCookieExpired()) {
                        startActivity(Intent(requireActivity(), LoginActivity::class.java))
                    } else {
                        showLogOutDialog()
                    }
                    true
                }
                title = if (RequestHelper.isCookieExpired()) {
                    "Go to login"
                } else {
                    "Logout"
                }
            }
        }

        private fun showLogOutDialog() {
            MaterialAlertDialogBuilder(requireContext())
                .setMessage("Are you sure?")
                .setPositiveButton("yep") { _, _ ->
                    UserCenter.logout()
                    EventBus.getDefault().post(LogoutEvent())
                    requireActivity().finish()
                }
                .setNegativeButton("no") { dialog, _ ->
                    dialog.dismiss()
                }.show()
        }

    }
}