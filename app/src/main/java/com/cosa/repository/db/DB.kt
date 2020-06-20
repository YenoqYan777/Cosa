package com.cosa.repository.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cosa.models.*
import com.cosa.repository.db.dao.*

@Database(
    entities = [(Things::class),
        (Notes::class),
        (Settings::class),
        (DeletedThings::class),
        (DeletedNotes::class)],
    version = 8,
    exportSchema = false
)
abstract class DB : RoomDatabase() {
    abstract fun thingAddedDao(): ThingsDao
    abstract fun notesDao(): NotesDao
    abstract fun settingsDao(): SettingsDao
    abstract fun deletedThingAddedDao(): DeletedThingsDao
    abstract fun deletedNotes(): DeletedNotesDao


}