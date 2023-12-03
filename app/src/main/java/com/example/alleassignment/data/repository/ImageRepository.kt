package com.example.alleassignment.data.repository

import android.net.Uri
import com.example.alleassignment.data.dao.ImageDao
import com.example.alleassignment.data.entity.ImageEntity
import com.example.alleassignment.data.model.Image
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ImageRepository(private val imageDao: ImageDao) {

    suspend fun insertImage(image: Image) {
        withContext(Dispatchers.IO) {
            image.uri?.path?.let {
                imageDao.insertImage(
                    ImageEntity(
                        it,
                        image.note ?: "",
                        image.text,
                        image.labels?.map { label -> label.text }?.toMutableList()
                    )
                )
            }
        }
    }

    suspend fun getImageData(imageUri: Uri): ImageEntity? {
        return withContext(Dispatchers.IO) {
            imageUri.path?.let {
                imageDao.getImage(it)
            }
        }
    }

    suspend fun updateImageLabels(image: Image) {
        withContext(Dispatchers.IO) {
            image.uri?.path?.let {
                imageDao.updateImageLabels(
                    it,
                    image.labels?.map { label -> label.text }?.toMutableList()
                )
            }
        }
    }
}