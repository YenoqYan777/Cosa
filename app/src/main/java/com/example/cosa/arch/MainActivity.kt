package com.example.cosa.arch

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.cosa.R
import com.example.cosa.arch.deletedItems.DeletedItemsFragment
import com.example.cosa.arch.helpers.LocalManager
import com.example.cosa.arch.notes.NotesFragment
import com.example.cosa.arch.settings.SettingsFragment
import com.example.cosa.arch.thingAdded.ThingAddedFragment
import com.example.cosa.databinding.ActivityMainBinding
import com.example.cosa.utils.ActivityUtils
import java.util.*


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
        ActivityUtils().pushFragment(
            thingAddedFragment,
            this.supportFragmentManager,
            R.id.fragment,
            true
        )
        onBottomNavClickListener()
    }

    private fun isStoragePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                true
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
                false
            }
        } else true
    }


    private fun onBottomNavClickListener() {
        var selectedFragment: Fragment? = null
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            (when (item.itemId) {
                R.id.home -> {
                    binding.bottomNavigationView.menu.getItem(2).isChecked = true
                    selectedFragment = thingAddedFragment
                    ActivityUtils().pushFragment(
                        selectedFragment!!,
                        this.supportFragmentManager,
                        R.id.fragment,
                        true
                    )
                    true
                }
                R.id.notes -> {
                    binding.bottomNavigationView.menu.getItem(1).isChecked = true
                    selectedFragment = notesFragment
                    ActivityUtils().pushFragment(
                        selectedFragment!!,
                        this.supportFragmentManager,
                        R.id.fragment,
                        true
                    )
                    true
                }
                R.id.recycle_bin -> {
                    binding.bottomNavigationView.menu.getItem(2).isChecked = true
                    selectedFragment = deletedItemsFragment
                    ActivityUtils().pushFragment(
                        selectedFragment!!,
                        this.supportFragmentManager,
                        R.id.fragment,
                        true
                    )
                    true
                }
                R.id.settings -> {
                    binding.bottomNavigationView.menu.getItem(2).isChecked = true
                    selectedFragment = settingsFragment
                    ActivityUtils().pushFragment(
                        selectedFragment!!,
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

    private fun getCurrentFragment(): Fragment? {
        return this.supportFragmentManager.findFragmentById(R.id.fragment)
    }

    private fun setBarItemChecked(currentFragment: Fragment) {
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

    override fun onBackPressed() {
        super.onBackPressed()
        val currentFragment = getCurrentFragment()
        setBarItemChecked(currentFragment!!)
    }
}
