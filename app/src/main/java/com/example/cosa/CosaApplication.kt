package com.example.cosa

import android.app.Application
import androidx.room.Room
import com.example.cosa.repository.db.DB

class CosaApplication : Application() {

    companion object {
        lateinit var dataBase: DB
    }

    override fun onCreate() {
        super.onCreate()
        dataBase = Room.databaseBuilder(
            this,
            DB::class.java, "thing_added_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}