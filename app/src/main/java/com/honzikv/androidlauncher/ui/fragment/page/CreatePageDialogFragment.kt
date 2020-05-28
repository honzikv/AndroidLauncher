package com.honzikv.androidlauncher.ui.fragment.page

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.honzikv.androidlauncher.databinding.CreatePageDialogFragmentBinding
import com.honzikv.androidlauncher.viewmodel.HomescreenViewModel
import com.honzikv.androidlauncher.viewmodel.SettingsViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

/**
 * Dialog pro vytvoreni nove stranky - jedna se o male okno, ktere se zobrazi ve spodu obrazovky
 * - bottom sheet
 */
class CreatePageDialogFragment private constructor() : BottomSheetDialogFragment() {

    companion object {

        /**
         * Konstanty pro zmenu vzhledu checkboxu
         */
        private val states = arrayOf(
            intArrayOf(-android.R.attr.state_checked),
            intArrayOf(android.R.attr.state_checked)
        )

        fun newInstance() = CreatePageDialogFragment()
    }

    private val homescreenViewModel: HomescreenViewModel by sharedViewModel()

    private val settingsViewModel: SettingsViewModel by sharedViewModel()

    private val checkboxThumbColors = intArrayOf(
        Color.WHITE,
        Color.BLACK
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = CreatePageDialogFragmentBinding.inflate(inflater)
        initialize(binding)
        return binding.root
    }

    /**
     * Inicializace UI
     */
    private fun initialize(binding: CreatePageDialogFragmentBinding) {
        //Nastaveni barev podle aktualniho tematu
        settingsViewModel.currentTheme.observe(viewLifecycleOwner, { theme ->
            val backgroundColor = theme.drawerSearchBackgroundColor
            val cardViewBackgroundColor = theme.drawerBackgroundColor
            val textFillColor = theme.drawerTextFillColor

            binding.apply {
                constraintLayout.setBackgroundColor(backgroundColor)
                cardView.setCardBackgroundColor(cardViewBackgroundColor)
                createPage.setTextColor(textFillColor)
                confirmButton.setColorFilter(textFillColor)
                pageAsFirstText.setTextColor(textFillColor)
                checkboxThumbColors[0] = theme.switchBackgroundColor
                checkboxThumbColors[1] = theme.switchThumbColorOn
                pageAsFirstCheckBox.buttonTintList = ColorStateList(states, checkboxThumbColors)
            }
        })

        //Potvrzeni prida novou stranku a ukonci dialogove okno
        binding.confirmButton.setOnClickListener {
            Timber.d("Adding page")
            homescreenViewModel.addPage(binding.pageAsFirstCheckBox.isChecked)
            dismiss()
        }
    }

}
