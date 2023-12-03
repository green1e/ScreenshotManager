package com.example.alleassignment.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.alleassignment.R
import com.example.alleassignment.data.model.Image
import com.example.alleassignment.databinding.ItemImageBinding
import com.example.alleassignment.util.loadImageWithFallBackDrawable
import com.example.alleassignment.ui.listener.OnScreenshotClickedListener

class ImageViewHolder(
    private val binding: ItemImageBinding,
    private val listener: OnScreenshotClickedListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(image: Image?) {
        binding.ivScreenshot.loadImageWithFallBackDrawable(image?.uri, null, R.color.white)
        binding.ivScreenshot.setOnClickListener {
            if (image != null) {
                listener.onScreenshotClicked(image)
            }
        }
    }
}