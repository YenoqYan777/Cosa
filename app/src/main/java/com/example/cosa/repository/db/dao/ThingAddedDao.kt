package com.example.cosa.repository.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.cosa.models.ThingAdded

@Dao
interface ThingAddedDao {
    @Query("SELECT * FROM thing_added")
    fun getAll(): LiveData<MutableList<ThingAdded>>

    @Query("SELECT * FROM thing_added WHERE id = :id")
    fun getThingAddedBy(id:Long): ThingAdded

    @Query("UPDATE thing_added SET thing = :name WHERE id = :id")
    fun updateThingName(id: Long, name: String?): Int

    @Query("UPDATE thing_added SET place = :place WHERE id = :id")
    fun updateThingPlace(id: Long, place: String?): Int

    @Query("UPDATE thing_added SET cacheUri = :cacheUri WHERE id = :id")
    fun updateThingImage(id: Long, cacheUri: String?): Int



    @Insert
    fun insertAll(vararg thingAdded: ThingAdded)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(thingAdded: ThingAdded)

    @Delete
    fun deleteItem(thingAdded: ThingAdded)

}