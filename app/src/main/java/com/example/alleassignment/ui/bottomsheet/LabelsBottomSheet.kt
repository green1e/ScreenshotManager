package com.example.alleassignment.ui.bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.alleassignment.R
import com.example.alleassignment.databinding.BottomSheetLabelsBinding
import com.example.alleassignment.ui.viewmodel.ScreenshotsViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.mlkit.vision.label.ImageLabel

class LabelsBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetLabelsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ScreenshotsViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            val dialog = this as BottomSheetDialog
            dialog.behavior.apply {
                this.state = BottomSheetBehavior.STATE_EXPANDED
                this.skipCollapsed = true
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetLabelsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnDone.setOnClickListener { dismiss() }
        binding.tvAddLabel.setOnClickListener { addLabel() }
        setLabels()
    }


    private fun setLabels() {
        viewModel.selectedImage.labels?.forEach { label ->
            binding.chipGroupLabels.addView(getChip(label))
        }
    }

    private fun addLabel() {
        if (binding.etLabel.text.isNullOrBlank()) return
        val label = ImageLabel(binding.etLabel.text.toString(), 1f, -1)
        binding.chipGroupLabels.addView(getChip(label))
        if (viewModel.selectedImage.labels == null) {
            viewModel.selectedImage.labels = mutableListOf(label)
        } else {
            viewModel.selectedImage.labels!!.add(label)
        }
        viewModel.isListUpdated.value = true
        binding.etLabel.text.clear()
    }

    private fun getChip(label: ImageLabel): Chip {
        return Chip(requireContext()).apply {
            tag = label
            text = label.text
            isClickable = false
            isCheckable = false
            isCloseIconVisible = true
            setCloseIconResource(R.drawable.ic_close_24)
            setOnCloseIconClickListener {
                binding.chipGroupLabels.removeView(this)
                viewModel.selectedImage.labels?.remove(tag as ImageLabel)
                viewModel.isListUpdated.value = true
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}