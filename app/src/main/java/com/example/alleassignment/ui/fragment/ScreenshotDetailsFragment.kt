package com.example.alleassignment.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.alleassignment.MainApplication
import com.example.alleassignment.R
import com.example.alleassignment.databinding.FragmentScreenshotDetailsBinding
import com.example.alleassignment.ui.bottomsheet.LabelsBottomSheet
import com.example.alleassignment.ui.viewmodel.ScreenshotDetailsViewModel
import com.example.alleassignment.ui.viewmodel.ScreenshotsViewModel
import com.example.alleassignment.util.gone
import com.example.alleassignment.util.loadImageWithFallBackDrawable
import com.example.alleassignment.util.show
import com.google.android.material.chip.Chip
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabel
import java.io.IOException


class ScreenshotDetailsFragment : Fragment() {

    private var _binding: FragmentScreenshotDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ScreenshotsViewModel by activityViewModels()
    private val screenshotDetailsViewModel: ScreenshotDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScreenshotDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addObservers()
        setupView()
        showScreenshotDetails()
    }

    private fun addObservers() {
        viewModel.isListUpdated.observe(viewLifecycleOwner) {
            if (it == true) {
                screenshotDetailsViewModel.updateImageLabels(viewModel.selectedImage)
                setLabels()
            }
        }
        screenshotDetailsViewModel.image.observe(viewLifecycleOwner) { imageEntity ->
            if (imageEntity == null && viewModel.selectedImage.uri != null) {
                val image = InputImage.fromFilePath(requireContext(), viewModel.selectedImage.uri!!)
                MainApplication.getRecognizer().process(image).addOnSuccessListener { visionText ->
                    var content = ""
                    visionText?.textBlocks?.forEach { block ->
                        content = content + " " + block.text
                    }
                    viewModel.selectedImage.text = content
                    screenshotDetailsViewModel.addImage(viewModel.selectedImage)
                    setDescription()
                }.addOnFailureListener {
                    binding.tvDescription.text = getString(R.string.description_not_available)
                }
                MainApplication.getLabeler().process(image).addOnSuccessListener { labels ->
                    viewModel.selectedImage.labels = labels
                    screenshotDetailsViewModel.addImage(viewModel.selectedImage)
                    setLabels()
                }.addOnFailureListener {
                    binding.tvCollections.show()
                    binding.chipGroupLabels.gone()
                }
            } else {
                viewModel.selectedImage.apply {
                    note = imageEntity?.note ?: ""
                    text = imageEntity?.text ?: ""
                    labels = imageEntity?.labels?.map {
                        ImageLabel(it, 1f, -1)
                    }?.toMutableList()
                }
                setNote()
                setDescription()
                setLabels()
            }
        }
    }

    private fun setupView() {
        binding.ivScreenshot.loadImageWithFallBackDrawable(
            this,
            viewModel.selectedImage.uri,
            binding.pbScreenshot,
            R.color.white
        )
        binding.tvEdit.setOnClickListener {
            LabelsBottomSheet().show(childFragmentManager, "")
        }
        binding.etNote.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.etNote.clearFocus()
                viewModel.selectedImage.note = binding.etNote.text.toString()
                screenshotDetailsViewModel.addImage(viewModel.selectedImage)
            }
            false
        }
    }

    private fun showScreenshotDetails() {
        try {
            viewModel.selectedImage.uri?.let {
                if (viewModel.selectedImage.note == null
                    || viewModel.selectedImage.text == null
                    || viewModel.selectedImage.labels == null
                ) {
                    screenshotDetailsViewModel.getImageData(it)
                } else {
                    setNote()
                    setDescription()
                    setLabels()
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun setNote() {
        binding.etNote.setText(viewModel.selectedImage.note ?: "")
    }

    private fun setDescription() {
        if (view == null) return
        var content = viewModel.selectedImage.text ?: ""
        if (content.isBlank()) content = getString(R.string.description_not_available)
        binding.tvDescription.text = content
    }

    private fun setLabels() {
        if (view == null) return
        binding.chipGroupLabels.removeAllViews()
        if (viewModel.selectedImage.labels.isNullOrEmpty()) {
            binding.tvCollections.show()
            binding.chipGroupLabels.gone()
        } else {
            binding.tvCollections.gone()
            binding.chipGroupLabels.show()
            viewModel.selectedImage.labels?.forEach { label ->
                val chip = Chip(requireContext()).apply {
                    text = label.text
                    isClickable = false
                    isCheckable = false
                }
                binding.chipGroupLabels.addView(chip)
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}