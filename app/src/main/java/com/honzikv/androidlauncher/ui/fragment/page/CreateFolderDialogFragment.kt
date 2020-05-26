package com.honzikv.androidlauncher.ui.fragment.page

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.observe
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.honzikv.androidlauncher.model.FolderModel
import com.honzikv.androidlauncher.model.PageModel
import com.honzikv.androidlauncher.databinding.CreateFolderDialogFragmentBinding
import com.honzikv.androidlauncher.viewmodel.HomescreenViewModel
import com.honzikv.androidlauncher.viewmodel.SettingsViewModel
import me.priyesh.chroma.ChromaDialog
import me.priyesh.chroma.ColorMode
import me.priyesh.chroma.ColorSelectListener
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber
import kotlin.properties.Delegates


class CreateFolderDialogFragment private constructor() : BottomSheetDialogFragment() {

    companion object {
        const val PAGE_ID = "pageId"

        fun newInstance(pageId: Long) = CreateFolderDialogFragment()
            .apply {
                arguments = Bundle().apply {
                    putLong(PAGE_ID, pageId)
                }
            }
    }

    private val homescreenViewModel: HomescreenViewModel by sharedViewModel()

    private val settingsViewModel: SettingsViewModel by sharedViewModel()

    private var pageId by Delegates.notNull<Long>()

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
        pageId = requireArguments()[PAGE_ID] as Long

        binding.confirmButton.setOnClickListener {
            if (binding.folderEditText.text.toString().isEmpty()) {
                Toast.makeText(context, "Please enter folder name", Toast.LENGTH_LONG)
                    .show()
            } else {
                val folder = FolderModel(
                    backgroundColor = backgroundColor,
                    itemColor = textColor,
                    title = binding.folderEditText.text.toString(),
                    pageId = pageId
                )
                homescreenViewModel.addFolderToPage(folder, pageId)
                dismiss()
            }
        }

        binding.backgroundColorCircle.also { backgroundColorCircle ->
            backgroundColorCircle.setColorFilter(backgroundColor)
            backgroundColorCircle.setOnClickListener {
                ChromaDialog.Builder()
                    .initialColor(backgroundColor)
                    .colorMode(ColorMode.ARGB)
                    .onColorSelected(object : ColorSelectListener {
                        override fun onColorSelected(color: Int) {
                            backgroundColor = color
                            backgroundColorCircle.setColorFilter(color)
                        }
                    })
                    .create()
                    .show(parentFragmentManager, "backgroundColorDialog")
            }
        }

        binding.textColorCircle.also { textColorCircle ->
            textColorCircle.setColorFilter(textColor)
            textColorCircle.setOnClickListener {
                ChromaDialog.Builder()
                    .initialColor(textColor)
                    .colorMode(ColorMode.HSV)
                    .onColorSelected(object : ColorSelectListener {
                        override fun onColorSelected(color: Int) {
                            textColor = color
                            textColorCircle.setColorFilter(color)
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
                folderEditText.setHintTextColor(textFillColor)
            }

        })
    }

}
