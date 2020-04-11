package com.example.cosa

import android.app.Application
import androidx.room.Room
import com.example.cosa.repository.db.DB

class CosaApplication : Application() {

    companion object{
       lateinit var thingAddedDB: DB
    }

    override fun onCreate() {
        super.onCreate()
        thingAddedDB = Room.databaseBuilder(
            this,
            DB::class.java, "thing_added_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}