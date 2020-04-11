package com.example.cosa.arch

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.cosa.R
import com.example.cosa.arch.notes.NotesFragment
import com.example.cosa.arch.settings.SettingsFragment
import com.example.cosa.arch.thingAdded.ThingAddedFragment
import com.example.cosa.databinding.ActivityMainBinding
import com.example.cosa.utils.ActivityUtils
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val thingAddedFragment: Fragment =
        ThingAddedFragment()

    private val settingsFragment: Fragment =
        SettingsFragment()

    private val notesFragment: Fragment =
        NotesFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        loadFragment(thingAddedFragment)
        onBottomNavClickListener()

    }

    private fun loadFragment(fragment: Fragment){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment, fragment)
            .commit()
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
            is SettingsFragment -> {
                binding.bottomNavigationView.menu.getItem(2).isChecked = true
            }
            else -> {
                binding.bottomNavigationView.menu.getItem(0).isChecked = true
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val currentFragment = getCurrentFragment()
        setBarItemChecked(currentFragment!!)
    }
}
