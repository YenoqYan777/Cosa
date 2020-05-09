package com.example.cosa.arch

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.cosa.R
import com.example.cosa.arch.deletedItems.DeletedItemsFragment
import com.example.cosa.arch.notes.NotesFragment
import com.example.cosa.arch.settings.SettingsFragment
import com.example.cosa.arch.thingAdded.ThingAddedFragment
import com.example.cosa.databinding.ActivityMainBinding
import com.example.cosa.extension.isStoragePermissionGranted
import com.example.cosa.extension.pushFragment
import com.example.cosa.helper.LocalManager


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val thingAddedFragment: Fragment =
        ThingAddedFragment()

    private val settingsFragment: Fragment =
        SettingsFragment()

    private val notesFragment: Fragment =
        NotesFragment()

    private val deletedItemsFragment: Fragment =
        DeletedItemsFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        isStoragePermissionGranted()
        pushFragment(
            thingAddedFragment,
            this.supportFragmentManager,
            R.id.fragment,
            true
        )
        onBottomNavClickListener()
    }

    private fun onBottomNavClickListener() {
        lateinit var selectedFragment: Fragment
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            (when (item.itemId) {
                R.id.home -> {
                    binding.bottomNavigationView.menu.getItem(0).isChecked = true
                    selectedFragment = thingAddedFragment
                    pushFragment(
                        selectedFragment,
                        this.supportFragmentManager,
                        R.id.fragment,
                        true
                    )
                    true
                }
                R.id.notes -> {
                    binding.bottomNavigationView.menu.getItem(1).isChecked = true
                    selectedFragment = notesFragment
                    pushFragment(
                        selectedFragment,
                        this.supportFragmentManager,
                        R.id.fragment,
                        true
                    )
                    true
                }
                R.id.recycle_bin -> {
                    binding.bottomNavigationView.menu.getItem(2).isChecked = true
                    selectedFragment = deletedItemsFragment
                    pushFragment(
                        selectedFragment,
                        this.supportFragmentManager,
                        R.id.fragment,
                        true
                    )
                    true
                }
                R.id.settings -> {
                    binding.bottomNavigationView.menu.getItem(3).isChecked = true
                    selectedFragment = settingsFragment
                    pushFragment(
                        selectedFragment,
                        this.supportFragmentManager,
                        R.id.fragment,
                        true
                    )
                    true
                }
                else -> false
            })
        }
    }

    fun setBarItemChecked(currentFragment: Fragment) {
        when (currentFragment) {
            is NotesFragment -> {
                binding.bottomNavigationView.menu.getItem(1).isChecked = true
            }
            is DeletedItemsFragment -> {
                binding.bottomNavigationView.menu.getItem(2).isChecked = true
            }
            is SettingsFragment -> {
                binding.bottomNavigationView.menu.getItem(3).isChecked = true
            }
            else -> {
                binding.bottomNavigationView.menu.getItem(0).isChecked = true
            }
        }
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
