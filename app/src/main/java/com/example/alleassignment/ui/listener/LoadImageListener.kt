package com.example.alleassignment.ui.listener

import android.net.Uri
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.annotation.DrawableRes

interface LoadImageListener {
    fun loadImage(
        view: ImageView, uri: Uri?,
        progressBar: ProgressBar?,
        @DrawableRes fallbackDrawableRes: Int,
        overrideSize: Boolean = false,
        overrideWidth: Int = 0,
        overrideHeight: Int = 0
    )
}