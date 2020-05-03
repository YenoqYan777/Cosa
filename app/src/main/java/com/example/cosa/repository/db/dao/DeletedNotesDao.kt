package com.example.cosa.repository.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.cosa.models.DeletedNotes
import com.example.cosa.models.Notes

@Dao
interface DeletedNotesDao {
    @Query("SELECT * FROM deleted_notes")
    fun getAll(): LiveData<MutableList<DeletedNotes>>

    @Query("UPDATE deleted_notes SET text = :text WHERE id = :id")
    fun updateNote(id: Long, text: String?): Int

    @Insert
    fun insertAll(vararg delNote: DeletedNotes)

    @Insert
    fun insert(delNote: DeletedNotes)

    @Delete
    fun deleteItem(delNote: DeletedNotes)
}