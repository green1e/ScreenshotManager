package com.example.alleassignment.ui.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.alleassignment.data.database.ImagesDatabase
import com.example.alleassignment.data.entity.ImageEntity
import com.example.alleassignment.data.model.Image
import com.example.alleassignment.data.repository.ImageRepository
import kotlinx.coroutines.launch

class ScreenshotDetailsViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: ImageRepository

    init {
        val imageDao = ImagesDatabase.getDatabase(application).imageDao()
        repository = ImageRepository(imageDao)
    }

    fun addImage(image: Image) {
        viewModelScope.launch {
            repository.insertImage(image)
        }
    }

    private val _image = MutableLiveData<ImageEntity?>()
    val image: LiveData<ImageEntity?> get() = _image

    fun getImageData(imageUri: Uri) {
        viewModelScope.launch {
            _image.value = repository.getImageData(imageUri)
        }
    }

    fun updateImageLabels(image: Image) {
        viewModelScope.launch {
            repository.updateImageLabels(image)
        }
    }
}