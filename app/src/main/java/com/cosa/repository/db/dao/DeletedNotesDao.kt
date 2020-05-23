package com.cosa.repository.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.cosa.models.DeletedNotes

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