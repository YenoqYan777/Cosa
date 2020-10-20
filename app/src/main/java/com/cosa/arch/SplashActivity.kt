package com.cosa.arch

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.cosa.R
import com.cosa.extension.setToolBarColor
import com.cosa.helper.LocalManager
import com.cosa.repository.SettingsStore

class SplashActivity : AppCompatActivity() {
    private val SPLASH_TIME_OUT: Long = 1000 // 1 sec
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolBarColor(R.color.mainDarkBckg)
        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, SPLASH_TIME_OUT)
    }
}
