package com.example.alleassignment.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images_table")
data class ImageEntity(
    @PrimaryKey(autoGenerate = false)
    val uri: String,
    var note: String = "",
    var text: String? = null,
    var labels: MutableList<String>? = null
)