package com.example.cosa.repository.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.cosa.models.Notes

@Dao
interface NotesDao {
    @Query("SELECT * FROM notes")
    fun getAll(): LiveData<MutableList<Notes>>

    @Query("UPDATE notes SET text = :text WHERE id = :id")
    fun updateNote(id: Long, text: String?): Int

    @Insert
    fun insertAll(vararg notes: Notes)

    @Insert
    fun insert(notes: Notes)

    @Delete
    fun deleteItem(notes: Notes)
}