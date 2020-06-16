package com.cosa.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "deleted_things")
data class DeletedThings @JvmOverloads constructor(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var thing: String = "",
    var place: String = "",
    var cacheUri: String = ""
)