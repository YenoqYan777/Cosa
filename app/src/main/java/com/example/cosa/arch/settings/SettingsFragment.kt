package com.example.cosa.arch.settings

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
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
        setList()
    }

    private fun setList() {
        val sharedPref = activity?.getSharedPreferences(SHARED, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPref!!.edit()
        val langList =
            listOf(getString(R.string.am), getString(R.string.ru), getString(R.string.en))
        val adapter: ArrayAdapter<String> =
            ArrayAdapter(activity!!, R.layout.simple_list_item_single_choice_1, langList)

        binding.langListSettings.adapter = adapter
        binding.langListSettings.choiceMode = ListView.CHOICE_MODE_SINGLE
        when (sharedPref.getString(LANGUAGE, "en")) {
            "am" -> {
                binding.langListSettings.setItemChecked(0, true)
            }
            "ru" -> {
                binding.langListSettings.setItemChecked(1, true)
            }
            "en" -> {
                binding.langListSettings.setItemChecked(2, true)
            }
        }
        binding.langListSettings.setOnItemClickListener { parent, view, position, id ->
            when (position) {
                0 -> {
                    editor.putString(LANGUAGE, "am")
                    editor.apply()
                    refreshActivity()
                }
                1 -> {
                    editor.putString(LANGUAGE, "ru")
                    editor.apply()
                    refreshActivity()
                }
                2 -> {
                    editor.putString(LANGUAGE, "en")
                    editor.apply()
                    refreshActivity()
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
