package com.cosa.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Notes @JvmOverloads constructor(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var title:String = "",
    var text: String = ""
)


