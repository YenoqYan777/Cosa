package com.cosa.repository.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.cosa.models.Notes

@Dao
interface NotesDao {
    @Query("SELECT * FROM notes")
    fun getAll(): LiveData<MutableList<Notes>>

    @Query("UPDATE notes SET text = :text WHERE id = :id")
    fun updateNote(id: Long, text: String?): Int

    @Query("UPDATE notes SET title = :title WHERE id = :id")
    fun updateTitle(id: Long, title: String?): Int

    @Insert
    fun insert(notes: Notes)

    @Delete
    fun deleteItem(notes: Notes)
}