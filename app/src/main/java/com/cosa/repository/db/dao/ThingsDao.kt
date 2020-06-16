package com.cosa.repository.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.cosa.models.Things

@Dao
interface ThingsDao {
    @Query("SELECT * FROM things")
    fun getAll(): LiveData<MutableList<Things>>

    @Query("SELECT * FROM things WHERE id = :id")
    fun getThingAddedBy(id: Long): Things

    @Query("UPDATE things SET thing = :name WHERE id = :id")
    fun updateThingName(id: Long, name: String?): Int

    @Query("UPDATE things SET place = :place WHERE id = :id")
    fun updateThingPlace(id: Long, place: String?): Int

    @Query("UPDATE things SET cacheUri = :cacheUri WHERE id = :id")
    fun updateThingImage(id: Long, cacheUri: String?): Int

    @Insert
    fun insertAll(vararg things: Things)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(things: Things)

    @Delete
    fun deleteItem(things: Things)
}