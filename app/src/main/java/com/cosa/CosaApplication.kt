package com.cosa

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.multidex.MultiDex
import androidx.room.Room
import com.cosa.helper.LocalManager
import com.cosa.repository.db.DB
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration

class CosaApplication : Application() {

    companion object {
        lateinit var dataBase: DB
    }

    override fun onCreate() {
        super.onCreate()

        MultiDex.install(this)
        MobileAds.initialize(this)
         if (BuildConfig.DEBUG){
             MobileAds.setRequestConfiguration(
                 RequestConfiguration.Builder().setTestDeviceIds(
                     listOf("55C88DD7646797FD180419FAB7D5BBD4")
                 ).build()
             )
         }
        dataBase = Room.databaseBuilder(
            this,
            DB::class.java, "thing_added_db"
        )
            .fallbackToDestructiveMigration()
            .build()
        val pref: SharedPreferences = this.getSharedPreferences(LocalManager.SHARED, Context.MODE_PRIVATE)

        if(pref.getString(LocalManager.THEME_KEY, "dark") == "light"){
            setTheme(R.style.AppTheme_NoActionBar_Light)
        }else{
            setTheme(R.style.AppTheme_NoActionBar)
        }
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocalManager.setLocale(base))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LocalManager.setLocale(this)
    }
}