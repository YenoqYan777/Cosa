package com.cosa.helper

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import com.cosa.repository.SettingsStore
import java.util.*


object LocalManager {
    const val SHARED = "sharedPref"
    const val LANGUAGE_KEY = "Language"
    const val THEME_KEY = "Theme"
    const val SAVE_TRASH_KEY: String = "SAVETRASH"
    const val SAVE_TRASH_KEY_NOTES: String = "savetrashfornotes"

    fun setLocale(context: Context): Context {
        return setNewLocale(
            context,
            getLanguage(context)
        )
    }

    fun setNewLocale(context: Context, language: String): Context {
        SettingsStore(context).persistLanguage(language)
        return updateResources(
            context,
            language
        )
    }

    private fun getLanguage(context: Context): String =
        SettingsStore(context).sharedPreferences.getString(
            LANGUAGE_KEY,
            defaultLanguage()
        )!!

    private fun defaultLanguage(): String {
        val locale: Locale = Locale.getDefault()
        if (locale.language == "en" || locale.language == "ru" || locale.language == "am") {
            return locale.language
        }
        return "en"
    }


    private fun updateResources(
        context: Context,
        language: String
    ): Context {
        var mContext: Context = context
        val locale = Locale(language)
        Locale.setDefault(locale)
        val res: Resources = mContext.resources
        val config = Configuration(res.configuration)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val localeList = LocaleList(locale)
            LocaleList.setDefault(localeList)
            config.setLocales(localeList)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            config.setLocale(locale)
        }
        if (mContext.applicationContext != null) {
            mContext.applicationContext.resources
                .updateConfiguration(config, res.displayMetrics)
        }
        mContext = mContext.createConfigurationContext(config)
        return mContext
    }
}