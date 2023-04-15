package com.yhezra.githubapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.yhezra.githubapp.helper.ViewModelFactory
import com.yhezra.githubapp.ui.home.HomeActivity
import com.yhezra.githubapp.ui.settings.SettingsPreferences
import com.yhezra.githubapp.ui.settings.SettingsViewModel
import java.util.*
import kotlin.concurrent.timerTask

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pref = SettingsPreferences.getInstance(dataStore)
        val settingsViewModel = ViewModelProvider(this, ViewModelFactory(pref = pref)).get(
            SettingsViewModel::class.java
        )

        settingsViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }


        Timer().schedule(timerTask {
            val moveToHomeActivity = Intent(this@MainActivity, HomeActivity::class.java)
            startActivity(moveToHomeActivity)
            finish()
        }, 3000)
    }
}