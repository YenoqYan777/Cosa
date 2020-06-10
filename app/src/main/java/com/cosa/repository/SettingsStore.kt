package com.cosa.repository

import android.content.Context
import android.content.SharedPreferences
import com.cosa.helper.LocalManager

class SettingsStore(val context: Context) {
    val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(LocalManager.SHARED, Context.MODE_PRIVATE)

    fun persistLanguage(
        language: String
    ) {
        sharedPreferences.edit().putString(LocalManager.LANGUAGE_KEY, language).apply()
    }

    fun setIsCheckedThings(isChecked: Boolean) {
        sharedPreferences.edit().run {
            putBoolean(LocalManager.SAVE_TRASH_KEY, isChecked)
            apply()
        }
    }

    fun setIsCheckedNotes(isChecked: Boolean) {
        sharedPreferences.edit().run {
            putBoolean(LocalManager.SAVE_TRASH_KEY_NOTES, isChecked)
            apply()
        }
    }

    fun setThemeKey(key: String) {
        sharedPreferences.edit().run {
            putString(LocalManager.THEME_KEY, key)
            apply()
        }
    }
}