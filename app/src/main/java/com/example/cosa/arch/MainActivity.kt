package com.example.cosa.arch

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.cosa.R
import com.example.cosa.databinding.ActivityMainBinding
import com.example.cosa.extension.isStoragePermissionGranted
import com.example.cosa.helper.LocalManager


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
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

}

