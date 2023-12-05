package com.example.alleassignment.ui.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.alleassignment.R
import com.example.alleassignment.data.model.Image
import com.example.alleassignment.databinding.FragmentScreenshotsBinding
import com.example.alleassignment.ui.adapter.ImagesAdapter
import com.example.alleassignment.ui.listener.LoadImageListener
import com.example.alleassignment.ui.listener.OnScreenshotClickedListener
import com.example.alleassignment.ui.viewmodel.ScreenshotsViewModel
import com.example.alleassignment.util.loadImageWithFallBackDrawable

class ScreenshotsFragment : Fragment() {

    private var _binding: FragmentScreenshotsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ScreenshotsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScreenshotsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivScreenshot.loadImageWithFallBackDrawable(
            this,
            viewModel.selectedImage.uri,
            binding.pbScreenshot,
            R.color.white
        )
        binding.rvScreenshots.adapter =
            ImagesAdapter(viewModel.imageList, object : OnScreenshotClickedListener {
                override fun onScreenshotClicked(image: Image) {
                    viewModel.selectedImage = image
                    binding.ivScreenshot.loadImageWithFallBackDrawable(
                        this@ScreenshotsFragment,
                        image.uri,
                        binding.pbScreenshot,
                        R.color.white
                    )
                }
            }, object : LoadImageListener {
                override fun loadImage(
                    view: ImageView,
                    uri: Uri?,
                    progressBar: ProgressBar?,
                    fallbackDrawableRes: Int,
                    overrideSize: Boolean,
                    overrideWidth: Int,
                    overrideHeight: Int
                ) {
                    view.loadImageWithFallBackDrawable(
                        this@ScreenshotsFragment,
                        uri,
                        progressBar,
                        fallbackDrawableRes,
                        overrideSize,
                        overrideWidth,
                        overrideHeight
                    )
                }
            })
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}