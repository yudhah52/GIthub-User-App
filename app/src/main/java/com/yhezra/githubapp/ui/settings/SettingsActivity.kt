package com.yhezra.githubapp.ui.settings

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.yhezra.githubapp.dataStore
import com.yhezra.githubapp.databinding.ActivitySettingsBinding
import com.yhezra.githubapp.helper.ViewModelFactory


class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingsPreferences.getInstance(dataStore)
        val settingsViewModel = ViewModelProvider(this, ViewModelFactory(pref = pref)).get(
            SettingsViewModel::class.java
        )

        binding.switchTheme.let {switchTheme->
            settingsViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
                if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    switchTheme.isChecked = true
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    switchTheme.isChecked = false
                }
            }

            switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
               settingsViewModel.saveThemeSetting(isChecked)
            }

        }




    }

//    private fun obtainViewModel(activity: AppCompatActivity,pref: SettingsPreferences): SettingsViewModel {
//        val factory = ViewModelFactory.getInstance(activity.application, )
//        return ViewModelProvider(activity, factory).get(SettingsViewModel::class.java)
//    }
}

