package com.example.cosa.repository.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.cosa.models.DeletedThingAdded
import com.example.cosa.models.ThingAdded

@Dao
interface DeletedThingsDao {
    @Query("SELECT * FROM deleted_thing_added")
    fun getAll(): LiveData<MutableList<DeletedThingAdded>>

    @Query("UPDATE deleted_thing_added SET thing = :name WHERE id = :id")
    fun updateDeletedThingName(id: Long, name: String?): Int

    @Query("UPDATE deleted_thing_added SET place = :place WHERE id = :id")
    fun updateDeletedThingPlace(id: Long, place: String?): Int

    @Query("UPDATE deleted_thing_added SET cacheUri = :cacheUri WHERE id = :id")
    fun updateDeletedThingImage(id: Long, cacheUri: String?): Int

    @Insert
    fun insertAll(vararg deletedThingAdded: DeletedThingAdded)

    @Insert
    fun insert(deletedThingAdded: DeletedThingAdded)

    @Delete
    fun deleteItem(deletedThingAdded: DeletedThingAdded)

}