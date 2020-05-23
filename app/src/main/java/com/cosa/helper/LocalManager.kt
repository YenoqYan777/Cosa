package com.cosa.helper

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import java.util.*


object LocalManager {
    const val SHARED = "sharedPref"
    const val LANGUAGE_KEY = "Language"
    const val SAVE_TRASH_KEY:String = "SAVETRASH"
    const val SAVE_TRASH_KEY_NOTES: String = "savetrashfornotes"

    fun setLocale(context: Context): Context {
        return setNewLocale(
            context,
            getLanguage(context)
        )
    }

    fun setNewLocale(context: Context, language: String): Context {
        persistLanguage(context, language)
        return updateResources(
            context,
            language
        )
    }

    fun getLanguage(context: Context): String {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(SHARED, Context.MODE_PRIVATE)
        return sharedPreferences.getString(
            LANGUAGE_KEY,
            defaultLanguage()
        )!!
    }

    private fun defaultLanguage(): String {
        val locale: Locale = Locale.getDefault()
        if (locale.language == "en" || locale.language == "ru" || locale.language == "am") {
            return locale.language
        }
        return "en"
    }

    private fun persistLanguage(
        context: Context,
        language: String
    ) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(SHARED, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(LANGUAGE_KEY, language).apply()
    }

    private fun updateResources(
        context: Context,
        language: String
    ): Context {
        var context: Context = context
        val locale = Locale(language)
        Locale.setDefault(locale)
        val res: Resources = context.resources
        val config = Configuration(res.configuration)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val localeList = LocaleList(locale)
            LocaleList.setDefault(localeList)
            config.setLocales(localeList)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            config.setLocale(locale)
        }
        if (context.applicationContext != null) {
            context.applicationContext.resources
                .updateConfiguration(config, res.displayMetrics)
        }
        context = context.createConfigurationContext(config)
        return context
    }
}