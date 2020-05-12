package com.honzikv.androidlauncher.ui.fragment.dialog

import android.R.color
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.observe
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.honzikv.androidlauncher.data.model.FolderModel
import com.honzikv.androidlauncher.data.model.PageModel
import com.honzikv.androidlauncher.databinding.CreateFolderDialogFragmentBinding
import com.honzikv.androidlauncher.viewmodel.HomescreenViewModel
import com.honzikv.androidlauncher.viewmodel.SettingsViewModel
import me.priyesh.chroma.ChromaDialog
import me.priyesh.chroma.ColorMode
import me.priyesh.chroma.ColorSelectListener
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel


class CreateFolderDialogFragment private constructor() : BottomSheetDialogFragment() {

    companion object {
        const val PAGE = "page"

        fun newInstance(pageWithFolders: PageModel) = CreateFolderDialogFragment().apply {
            arguments = Bundle().apply {
                putString(PAGE, Gson().toJson(pageWithFolders))
            }
        }
    }

    private val homescreenViewModel: HomescreenViewModel by viewModel()

    private val settingsViewModel: SettingsViewModel by viewModel()

    private lateinit var page: PageModel

    private var backgroundColor = Color.WHITE
    private var textColor = Color.DKGRAY

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = CreateFolderDialogFragmentBinding.inflate(inflater)
        initialize(binding)
        return binding.root
    }

    private fun initialize(binding: CreateFolderDialogFragmentBinding) {
        page = Gson().fromJson(requireArguments()[PAGE] as String, PageModel::class.java)

        binding.confirmButton.setOnClickListener {
            val folder = FolderModel(
                backgroundColor = backgroundColor,
                itemColor = textColor,
                title = binding.folderEditText.text.toString()
            )
            homescreenViewModel.addFolderToPage(folder, page)
            dismiss()
        }

        binding.backgroundColorCircle.also { backgroundColorCircle ->
            DrawableCompat.wrap(backgroundColorCircle.drawable).setTint(backgroundColor)
            backgroundColorCircle.setOnClickListener {
                ChromaDialog.Builder()
                    .initialColor(backgroundColor)
                    .colorMode(ColorMode.ARGB)
                    .onColorSelected(object : ColorSelectListener {
                        override fun onColorSelected(color: Int) {
                            backgroundColorCircle.setColorFilter(color)
                            backgroundColor = color
                        }
                    })
                    .create()
                    .show(parentFragmentManager, "backgroundColorDialog")
            }
        }

        binding.textColorCircle.also { textColorCircle ->
            DrawableCompat.wrap(textColorCircle.drawable).setTint(textColor)
            textColorCircle.setOnClickListener {
                ChromaDialog.Builder()
                    .initialColor(textColor)
                    .colorMode(ColorMode.HSV)
                    .onColorSelected(object : ColorSelectListener {
                        override fun onColorSelected(color: Int) {
                            textColorCircle.setColorFilter(color)
                            textColor = color
                        }
                    })
                    .create()
                    .show(parentFragmentManager, "textColorDialog")
            }
        }

        settingsViewModel.currentTheme.observe(viewLifecycleOwner, { theme ->
            val cardViewTextColor = theme.drawerTextFillColor
            val backgroundColor = theme.drawerSearchBackgroundColor
            val cardViewBackgroundColor = theme.drawerBackgroundColor
            val textFillColor = theme.drawerTextFillColor

            binding.apply {
                constraintLayout.setBackgroundColor(backgroundColor)
                cardView.setCardBackgroundColor(cardViewBackgroundColor)
                createFolder.setTextColor(textFillColor)
                textColor.setTextColor(textFillColor)
                backgroundColorText.setTextColor(textFillColor)
                confirmButton.setColorFilter(textFillColor)
                folderEditText.setTextColor(textFillColor)
                folderEditText.backgroundTintList = ColorStateList.valueOf(cardViewTextColor)
            }

        })
    }

}
