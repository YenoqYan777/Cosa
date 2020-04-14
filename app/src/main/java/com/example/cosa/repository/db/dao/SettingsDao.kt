package com.example.cosa.repository.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.cosa.models.Notes
import com.example.cosa.models.Settings

@Dao
interface SettingsDao {
    @Query("SELECT * FROM settings")
    fun getAll(): LiveData<MutableList<Settings>>

    @Insert
    fun insertAll(vararg settings: Settings)

    @Insert
    fun insert(settings: Settings)

}