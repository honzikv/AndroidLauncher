package com.honzikv.androidlauncher.ui.fragment.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View

import androidx.lifecycle.observe
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

import com.honzikv.androidlauncher.databinding.CreatePageDialogFragmentBinding
import com.honzikv.androidlauncher.viewmodel.HomescreenViewModel
import com.honzikv.androidlauncher.viewmodel.SettingsViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class CreatePageDialogFragment private constructor(): BottomSheetDialogFragment() {

    companion object {
        fun newInstance() =
            CreatePageDialogFragment()
    }

    private val homescreenViewModel: HomescreenViewModel by sharedViewModel()

    private val settingsViewModel: SettingsViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = CreatePageDialogFragmentBinding.inflate(inflater)
        initialize(binding)
        return binding.root
    }

    private fun initialize(binding: CreatePageDialogFragmentBinding) {
        settingsViewModel.currentTheme.observe(viewLifecycleOwner, { theme ->
            val cardViewTextColor = theme.drawerTextFillColor
            val backgroundColor = theme.drawerSearchBackgroundColor
            val cardViewBackgroundColor = theme.drawerBackgroundColor
            val textFillColor = theme.drawerTextFillColor

            binding.apply {
                constraintLayout.setBackgroundColor(backgroundColor)
                cardView.setCardBackgroundColor(cardViewBackgroundColor)
                createPage.setTextColor(textFillColor)
                confirmButton.setColorFilter(textFillColor)
                pageAsFirstText.setTextColor(textFillColor)
                //todo checkbox
            }
        })

        binding.confirmButton.setOnClickListener {
            Timber.d("Adding page")
            homescreenViewModel.addPage(binding.pageAsFirstCheckBox.isChecked)
            dismiss()
        }
    }

}