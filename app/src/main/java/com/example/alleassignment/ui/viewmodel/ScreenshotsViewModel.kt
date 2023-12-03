package com.example.alleassignment.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.alleassignment.data.model.Image

class ScreenshotsViewModel : ViewModel() {

    var selectedImage = Image()

    val imageList = arrayListOf<Image>()

    var isListUpdated = MutableLiveData<Boolean>()
}