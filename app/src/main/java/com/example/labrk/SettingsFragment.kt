package com.example.labrk

import android.os.Bundle
import android.widget.Toast
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        val editTextPreference: EditTextPreference? = findPreference("limit")
        editTextPreference!!.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
            val regex = Regex(pattern = "[А-Яа-яA-Za-z]")
            if (newValue.toString().contains(regex)) {
                Toast.makeText(context, "Разрешены только цифры!", Toast.LENGTH_LONG).show()
                return@OnPreferenceChangeListener false
            }
            return@OnPreferenceChangeListener true
        }
    }
}