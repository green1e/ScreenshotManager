package com.example.alleassignment.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alleassignment.data.model.Image
import com.example.alleassignment.databinding.ItemImageBinding
import com.example.alleassignment.ui.listener.OnScreenshotClickedListener

class ImagesAdapter(
    private val images: ArrayList<Image>?,
    private val listener: OnScreenshotClickedListener
) : RecyclerView.Adapter<ImageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            ItemImageBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), listener
        )
    }

    override fun getItemCount(): Int {
        return images?.size ?: 0
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(images?.getOrNull(position))
    }
}