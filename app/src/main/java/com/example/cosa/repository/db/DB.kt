package com.example.cosa.repository.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cosa.models.Notes
import com.example.cosa.models.ThingAdded
import com.example.cosa.repository.db.dao.NotesDao
import com.example.cosa.repository.db.dao.ThingAddedDao

@Database(entities = [(ThingAdded::class), (Notes::class)], version = 2)
abstract class DB : RoomDatabase() {
    abstract fun thingAddedDao(): ThingAddedDao
    abstract fun notesDao():NotesDao

}