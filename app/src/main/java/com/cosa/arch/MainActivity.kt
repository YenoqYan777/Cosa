package com.cosa.arch

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.cosa.R
import com.cosa.databinding.ActivityMainBinding
import com.cosa.extension.isStoragePermissionGranted
import com.cosa.extension.setToolBarColor
import com.cosa.helper.LocalManager
import com.cosa.repository.SettingsStore


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolBarColor(R.color.mainDarkBckg)
        if (SettingsStore(this).sharedPreferences.getString(
                LocalManager.THEME_KEY,
                "dark"
            ) == "light"
        ) {
            setTheme(R.style.AppTheme_NoActionBar_Light)
        } else {
            setTheme(R.style.AppTheme_NoActionBar)
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.bottomNavigationView.visibility = View.VISIBLE
        isStoragePermissionGranted()
        setUpNavigation()
    }

    private fun setUpNavigation() {
        navController = Navigation.findNavController(this, R.id.fragment)
        NavigationUI.setupWithNavController(
            binding.bottomNavigationView,
            navController
        )
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocalManager.setLocale(newBase))
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        if (overrideConfiguration != null) {
            val uiMode = overrideConfiguration.uiMode
            overrideConfiguration.setTo(baseContext.resources.configuration)
            overrideConfiguration.uiMode = uiMode
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        if (count == 0) {
            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStack()
        }
    }
}

