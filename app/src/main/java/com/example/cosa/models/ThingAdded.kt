package com.example.cosa.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "thing_added")
data class ThingAdded @JvmOverloads constructor(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    var thing: String = "",

    var place: String = "",

    var cacheUri: String = ""

)