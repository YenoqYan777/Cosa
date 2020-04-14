package com.example.cosa.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class Settings @JvmOverloads constructor(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var value: String = ""
)