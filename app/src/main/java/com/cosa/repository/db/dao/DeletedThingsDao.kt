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

    @Query("UPDATE deleted_things SET thing = :name WHERE id = :id")
    fun updateDeletedThingName(id: Long, name: String?): Int

    @Query("UPDATE deleted_things SET place = :place WHERE id = :id")
    fun updateDeletedThingPlace(id: Long, place: String?): Int

    @Query("UPDATE deleted_things SET cacheUri = :cacheUri WHERE id = :id")
    fun updateDeletedThingImage(id: Long, cacheUri: String?): Int

    @Insert
    fun insertAll(vararg deletedThings: DeletedThings)

    @Insert
    fun insert(deletedThings: DeletedThings)

    @Delete
    fun deleteItem(deletedThings: DeletedThings)

}