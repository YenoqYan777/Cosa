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
import com.example.cosa.helper.LocalManager
import com.example.cosa.helper.LocalManager.LANGUAGE_KEY
import com.example.cosa.helper.LocalManager.SAVE_TRASH_KEY
import com.example.cosa.helper.LocalManager.SAVE_TRASH_KEY_NOTES
import com.example.cosa.helper.LocalManager.SHARED


class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var pref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_settings, container, false
        )
        pref = requireActivity().getSharedPreferences(SHARED, Context.MODE_PRIVATE)
        binding.backUpThings.isChecked = pref.getBoolean(SAVE_TRASH_KEY, true)
        binding.backUpNotes.isChecked = pref.getBoolean(SAVE_TRASH_KEY_NOTES, true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setList()
        setSwitcherListener()
    }

    private fun setSwitcherListener() {
        binding.backUpThings.setOnCheckedChangeListener { buttonView, isChecked ->
            pref.edit().run {
                putBoolean(SAVE_TRASH_KEY, isChecked)
                apply()
            }
        }
        binding.backUpNotes.setOnCheckedChangeListener { buttonView, isChecked ->
            pref.edit().run {
                putBoolean(SAVE_TRASH_KEY_NOTES, isChecked)
                apply()
            }
        }
    }

    private fun setList() {
        val langList =
            listOf(getString(R.string.am), getString(R.string.ru), getString(R.string.en))
        val adapter: ArrayAdapter<String> =
            ArrayAdapter(requireActivity(), R.layout.simple_list_item_single_choice_1, langList)

        binding.langListSettings.adapter = adapter
        binding.langListSettings.choiceMode = ListView.CHOICE_MODE_SINGLE
        when (pref.getString(LANGUAGE_KEY, "en")) {
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
                    LocalManager.setNewLocale(requireActivity(), "am")
                    refreshActivity()
                }
                1 -> {
                    LocalManager.setNewLocale(requireActivity(), "ru")
                    refreshActivity()
                }
                2 -> {
                    LocalManager.setNewLocale(requireActivity(), "en")
                    refreshActivity()
                }
            }
        }
    }

    private fun refreshActivity() {
        val intent = requireActivity().intent
        intent.addFlags(
            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    or Intent.FLAG_ACTIVITY_NO_ANIMATION
        )
        requireActivity().overridePendingTransition(0, 0)
        requireActivity().finish()

        requireActivity().overridePendingTransition(0, 0)
        startActivity(intent)
    }
}
