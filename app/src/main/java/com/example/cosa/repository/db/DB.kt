package com.example.cosa.repository.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cosa.models.DeletedThingAdded
import com.example.cosa.models.Notes
import com.example.cosa.models.Settings
import com.example.cosa.models.ThingAdded
import com.example.cosa.repository.db.dao.DeletedThingsDao
import com.example.cosa.repository.db.dao.NotesDao
import com.example.cosa.repository.db.dao.SettingsDao
import com.example.cosa.repository.db.dao.ThingAddedDao

@Database(entities = [(ThingAdded::class), (Notes::class), (Settings::class), (DeletedThingAdded::class)], version = 4)
abstract class DB : RoomDatabase() {
    abstract fun thingAddedDao(): ThingAddedDao
    abstract fun notesDao():NotesDao
    abstract fun settingsDao(): SettingsDao
    abstract fun deletedThingAddedDao(): DeletedThingsDao
}