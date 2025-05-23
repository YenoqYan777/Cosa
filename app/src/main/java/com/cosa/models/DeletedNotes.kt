package com.cosa.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deleted_notes")
data class DeletedNotes @JvmOverloads constructor(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var title: String = "",
    var text: String = ""


)


