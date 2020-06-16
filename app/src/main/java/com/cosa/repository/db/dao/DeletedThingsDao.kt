package com.cosa.repository.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.cosa.models.DeletedThings

@Dao
interface DeletedThingsDao {
    @Query("SELECT * FROM deleted_things")
    fun getAll(): LiveData<MutableList<DeletedThings>>

    @Insert
    fun insert(deletedThings: DeletedThings)

    @Delete
    fun deleteItem(deletedThings: DeletedThings)
}