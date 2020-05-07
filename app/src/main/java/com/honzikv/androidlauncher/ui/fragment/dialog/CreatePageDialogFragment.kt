package com.honzikv.androidlauncher.ui.fragment.dialog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

import com.honzikv.androidlauncher.R
import com.honzikv.androidlauncher.databinding.CreatePageDialogFragmentBinding
import com.honzikv.androidlauncher.viewmodel.HomescreenViewModel
import org.koin.android.ext.android.inject
import timber.log.Timber

class CreatePageDialogFragment : BottomSheetDialogFragment() {

    companion object {
        fun newInstance() = CreatePageDialogFragment()
    }

    private val homescreenViewModel: HomescreenViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = CreatePageDialogFragmentBinding.inflate(inflater)
        initialize(binding)
        return binding.root
    }

    private fun initialize(binding: CreatePageDialogFragmentBinding) {
        binding.confirmButton.setOnClickListener {
            Timber.d("Adding page")
            homescreenViewModel.addPage(binding.pageAsFirstCheckBox.isChecked)
            dismiss()
        }
    }

}
