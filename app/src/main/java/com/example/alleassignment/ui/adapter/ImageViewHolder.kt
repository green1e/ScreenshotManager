package com.example.alleassignment.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.alleassignment.R
import com.example.alleassignment.data.model.Image
import com.example.alleassignment.databinding.ItemImageBinding
import com.example.alleassignment.ui.listener.LoadImageListener
import com.example.alleassignment.ui.listener.OnScreenshotClickedListener

class ImageViewHolder(
    private val binding: ItemImageBinding,
    private val listener: OnScreenshotClickedListener,
    private val loadImageListener: LoadImageListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(image: Image?) {
        if (image?.uri == null) return
        /*val bitmap = binding.ivScreenshot.context.contentResolver.loadThumbnail(
            image.uri,
            Size(100, 200),
            null
        )
        binding.ivScreenshot.loadImageBitmapWithFallBackDrawable(bitmap, null, R.color.white)*/
        loadImageListener.loadImage(
            binding.ivScreenshot,
            image.uri,
            null,
            R.color.white,
            true
        )
        binding.ivScreenshot.setOnClickListener {
            listener.onScreenshotClicked(image)
        }
    }
}