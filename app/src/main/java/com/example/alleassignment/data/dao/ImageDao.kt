package com.example.alleassignment.data.dao

import android.net.Uri
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.alleassignment.data.entity.ImageEntity

@Dao
interface ImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(imageEntity: ImageEntity)

    @Query("SELECT * FROM images_table WHERE uri = :uri LIMIT 1")
    suspend fun getImage(uri: String): ImageEntity?

    @Query("UPDATE images_table SET labels = :labels WHERE uri = :uri")
    suspend fun updateImageLabels(uri: String, labels: MutableList<String>?)
}