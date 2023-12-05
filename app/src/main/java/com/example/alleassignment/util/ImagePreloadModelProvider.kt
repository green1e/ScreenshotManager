package com.example.alleassignment.util

import androidx.fragment.app.Fragment
import com.bumptech.glide.ListPreloader
import com.bumptech.glide.RequestBuilder
import com.example.alleassignment.R
import com.example.alleassignment.data.model.Image

class ImagePreloadModelProvider(
    private val fragment: Fragment,
    private val images: ArrayList<Image>?,
    private val width: Int,
    private val height: Int
) : ListPreloader.PreloadModelProvider<Image?> {
    override fun getPreloadItems(position: Int): MutableList<Image?> {
        val image = images?.getOrNull(position)
        return if (image == null) mutableListOf() else mutableListOf(image)
    }

    override fun getPreloadRequestBuilder(item: Image): RequestBuilder<*> {
        return getImageRequestBuilder(fragment, item.uri, null, R.color.white, true, width, height)
    }
}