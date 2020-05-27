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
import com.cosa.helper.LocalManager

class SplashActivity : AppCompatActivity() {
    private val SPLASH_TIME_OUT: Long = 1000 // 1 sec
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    setToolbarColor()
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



    private fun setToolbarColor() {
        this.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.window.statusBarColor = ContextCompat.getColor(this, R.color.darkerBckg)
        }
    }
}
