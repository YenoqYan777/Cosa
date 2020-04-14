package com.example.cosa.arch.settings

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.cosa.R
import com.example.cosa.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {
    private val SHARED: String = "sharedPref"
    private val LANGUAGE: String = "Language"
    private lateinit var adapter: ArrayAdapter<CharSequence>
    private lateinit var binding: FragmentSettingsBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_settings, container, false
        )
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSpinner()
    }

    private fun setSpinner() {
        val sharedPref = activity?.getSharedPreferences(SHARED, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPref!!.edit()
        adapter = ArrayAdapter.createFromResource(
            activity!!.baseContext,
            R.array.languages,
            R.layout.spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        editor.putString(LANGUAGE, "am")
                        editor.apply()
                        Toast.makeText(
                            activity, getString(
                                R.string.restart_to_save_changes
                            ), Toast.LENGTH_LONG
                        ).show()
                    }
                    1 -> {
                        editor.putString(LANGUAGE, "ru")
                        editor.apply()
                        Toast.makeText(
                            activity,
                            getString(R.string.restart_to_save_changes),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    2 -> {
                        editor.putString(LANGUAGE, "en")
                        editor.apply()
                        Toast.makeText(
                            activity,
                            getString(R.string.restart_to_save_changes),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    fun refreshActivity() {
        val intent = activity!!.intent
        intent.addFlags(
            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    or Intent.FLAG_ACTIVITY_NO_ANIMATION
        )
        activity!!.overridePendingTransition(0, 0)
        activity!!.finish()

        activity!!.overridePendingTransition(0, 0)
        startActivity(intent)
    }
}
