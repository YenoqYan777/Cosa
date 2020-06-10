package com.cosa.arch

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.cosa.R
import com.cosa.extension.setToolBarColor
import com.cosa.helper.LocalManager

class SplashActivity : AppCompatActivity() {
    private val SPLASH_TIME_OUT: Long = 1000 // 1 sec
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolBarColor(R.color.mainDarkBckg)

        val pref: SharedPreferences =
            this.getSharedPreferences(LocalManager.SHARED, Context.MODE_PRIVATE)

        if (pref.getString(LocalManager.THEME_KEY, "dark") == "light") {
            setTheme(R.style.AppTheme_NoActionBar_Light)
        } else {
            setTheme(R.style.AppTheme_NoActionBar)
        }
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, SPLASH_TIME_OUT)
    }
}
