package com.example.cosa

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.room.Room
import com.example.cosa.helper.LocalManager
import com.example.cosa.repository.db.DB
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration

class CosaApplication : Application() {

    companion object {
        lateinit var dataBase: DB
    }

    override fun onCreate() {
        super.onCreate()
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
    }


    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocalManager.setLocale(base))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LocalManager.setLocale(this)
    }
}