package com.cosa.arch.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.cosa.R
import com.cosa.arch.MainActivity
import com.cosa.databinding.FragmentSettingsBinding
import com.cosa.extension.setToolBarColor
import com.cosa.helper.LocalManager
import com.cosa.helper.LocalManager.LANGUAGE_KEY
import com.cosa.helper.LocalManager.SAVE_TRASH_KEY
import com.cosa.helper.LocalManager.SAVE_TRASH_KEY_NOTES
import com.cosa.helper.LocalManager.THEME_KEY
import com.cosa.repository.SettingsStore
import kotlinx.android.synthetic.main.activity_main.*

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var settingsStore: SettingsStore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().setToolBarColor(R.color.mainDarkBckg)
        requireActivity().bottomNavigationView.visibility = View.VISIBLE
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_settings, container, false
        )
        settingsStore = SettingsStore(requireActivity())
        binding.backUpThings.isChecked =
            settingsStore.sharedPreferences.getBoolean(SAVE_TRASH_KEY, true)
        binding.backUpNotes.isChecked =
            settingsStore.sharedPreferences.getBoolean(SAVE_TRASH_KEY_NOTES, true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLangList()
        setThemeList()
        setSwitcherListener()
    }

    private fun setSwitcherListener() {
        binding.backUpThings.setOnCheckedChangeListener { buttonView, isChecked ->
            settingsStore.setIsCheckedThings(isChecked)
        }
        binding.backUpNotes.setOnCheckedChangeListener { buttonView, isChecked ->
            settingsStore.setIsCheckedNotes(isChecked)
        }
    }

    private fun setLangList() {
        val langList =
            listOf(getString(R.string.am), getString(R.string.ru), getString(R.string.en))
        val adapter: ArrayAdapter<String> =
            ArrayAdapter(requireActivity(), R.layout.simple_list_item_single_choice_1, langList)

        binding.langListSettings.adapter = adapter
        binding.langListSettings.choiceMode = ListView.CHOICE_MODE_SINGLE
        when (settingsStore.sharedPreferences.getString(LANGUAGE_KEY, "en")) {
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

    private fun setThemeList() {
        val themeList =
            listOf(getString(R.string.dark), getString(R.string.light))
        val adapter: ArrayAdapter<String> =
            ArrayAdapter(requireActivity(), R.layout.simple_list_item_single_choice_1, themeList)

        binding.themeListSettings.adapter = adapter
        binding.themeListSettings.choiceMode = ListView.CHOICE_MODE_SINGLE
        when (settingsStore.sharedPreferences.getString(THEME_KEY, "dark")) {
            "dark" -> {
                binding.themeListSettings.setItemChecked(0, true)
            }
            "light" -> {
                binding.themeListSettings.setItemChecked(1, true)
            }
        }

        binding.themeListSettings.setOnItemClickListener { parent, view, position, id ->
            when (position) {
                0 -> {
                    settingsStore.setThemeKey("dark")
                    requireActivity().finish()
                    restartApp()
                }
                1 -> {
                    settingsStore.setThemeKey("light")
                    requireActivity().finish()
                    restartApp()
                }
            }
        }
    }

    private fun restartApp() {
        val i = Intent(activity, MainActivity::class.java)
        startActivity(i)
        requireActivity().fragmentManager.popBackStackImmediate()
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
