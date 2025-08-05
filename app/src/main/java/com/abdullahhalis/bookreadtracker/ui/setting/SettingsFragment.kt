package com.abdullahhalis.bookreadtracker.ui.setting

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreference
import com.abdullahhalis.bookreadtracker.R
import com.abdullahhalis.bookreadtracker.notification.DailyReminder
import com.abdullahhalis.bookreadtracker.util.DarkMode
import java.util.Locale

class SettingsFragment : PreferenceFragmentCompat() {

    private lateinit var prefManager: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var dailyReminder: DailyReminder

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val darkModePref = findPreference<ListPreference>(getString(R.string.pref_key_dark))
        darkModePref?.setOnPreferenceChangeListener { _, newValue ->
            val mode = DarkMode.valueOf((newValue as String).uppercase(Locale.US))
            updateTheme(mode.value)
        }

        val notificationPref = findPreference<SwitchPreference>(getString(R.string.pref_key_notify))
        notificationPref?.setOnPreferenceChangeListener { _, newValue ->
            if (newValue as Boolean) {
                dailyReminder.setDailyReminder(requireActivity())
            } else {
                dailyReminder.cancelAlarm(requireActivity())
            }
            true
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        prefManager = PreferenceManager.getDefaultSharedPreferences(requireContext())
        editor = prefManager.edit()
        dailyReminder = DailyReminder()
    }

    private fun updateTheme(nightMode: Int): Boolean {
        AppCompatDelegate.setDefaultNightMode(nightMode)
        requireActivity().recreate()
        return true
    }
}