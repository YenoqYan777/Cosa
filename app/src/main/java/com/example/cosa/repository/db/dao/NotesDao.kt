package com.example.cosa.repository.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.cosa.models.Notes

@Dao
interface NotesDao {
    @Query("SELECT * FROM notes")
    fun getAll(): LiveData<MutableList<Notes>>

    @Insert
    fun insertAll(vararg notes: Notes)

    @Insert
    fun insert(notes: Notes)


}