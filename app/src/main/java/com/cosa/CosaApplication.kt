package com.cosa
import com.cosa.R
import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.multidex.MultiDex
import androidx.room.Room
import com.cosa.helper.LocalManager
import com.cosa.repository.SettingsStore
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

        if(SettingsStore(this).sharedPreferences.getString(LocalManager.THEME_KEY, "dark") == "light"){
            this.setTheme(R.style.AppTheme_NoActionBar_Light)
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