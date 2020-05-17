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

    @Insert
    fun insertAll(vararg delNote: DeletedNotes)

    @Insert
    fun insert(delNote: DeletedNotes)

    @Delete
    fun deleteItem(delNote: DeletedNotes)
}