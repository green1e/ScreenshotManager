package com.example.alleassignment.data.model

import android.net.Uri
import com.google.mlkit.vision.label.ImageLabel
import com.google.mlkit.vision.text.Text

data class Image(
    val uri: Uri? = null,
    val name: String? = null,
    val size: Int? = null,
    val relativePath: String? = null,
    var position: Int? = null,
    var note: String? = null,
    var text: String? = null,
    var labels: MutableList<ImageLabel>? = null
)