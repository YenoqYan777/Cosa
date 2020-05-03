package com.example.cosa.repository.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cosa.models.*
import com.example.cosa.repository.db.dao.*

@Database(
    entities = [(ThingAdded::class),
        (Notes::class),
        (Settings::class),
        (DeletedThingAdded::class),
        (DeletedNotes::class)],
    version = 6
)
abstract class DB : RoomDatabase() {
    abstract fun thingAddedDao(): ThingAddedDao
    abstract fun notesDao(): NotesDao
    abstract fun settingsDao(): SettingsDao
    abstract fun deletedThingAddedDao(): DeletedThingsDao
    abstract fun deletedNotes(): DeletedNotesDao
}