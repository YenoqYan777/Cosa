package com.cosa
import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.multidex.MultiDex
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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
        val migration = object : Migration(8, 9){
            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("ALTER TABLE 'notes' ADD COLUMN 'title' TEXT NOT NULL DEFAULT ''")
                database.execSQL("ALTER TABLE 'deleted_notes' ADD COLUMN 'title' TEXT NOT NULL DEFAULT ''")
            }

        }
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
            .addMigrations(migration)
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