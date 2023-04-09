package com.yhezra.githubapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.yhezra.githubapp.ui.home.HomeActivity
import java.util.*
import kotlin.concurrent.timerTask

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        Timer().schedule(timerTask {
            val moveToHomeActivity = Intent(this@MainActivity, HomeActivity::class.java)
            startActivity(moveToHomeActivity)
            finish()
        }, 3000)
    }
}