package com.honzikv.androidlauncher.ui.fragment.dialog

import android.app.AlertDialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.graphics.drawable.DrawableCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson

import androidx.lifecycle.observe
import com.honzikv.androidlauncher.SEMITRANSPARENT_STROKE_COLOR

import com.honzikv.androidlauncher.data.model.FolderModel
import com.honzikv.androidlauncher.databinding.FolderSettingsFragmentBinding
import com.honzikv.androidlauncher.viewmodel.HomescreenViewModel
import com.honzikv.androidlauncher.viewmodel.SettingsViewModel
import kotlinx.android.synthetic.main.settings_page_item.*
import me.priyesh.chroma.ChromaDialog
import me.priyesh.chroma.ColorMode
import me.priyesh.chroma.ColorSelectListener
import org.koin.android.ext.android.bind
import org.koin.android.ext.android.inject
import timber.log.Timber

class EditFolderSettingsDialogFragment : BottomSheetDialogFragment() {

    private val homescreenViewModel: HomescreenViewModel by inject()

    private val settingsViewModel: SettingsViewModel by inject()

    companion object {
        const val FOLDER = "folder"

        /**
         * [bundle] containing serialized folderModel
         */
        fun newInstance(folderModel: FolderModel) =
            EditFolderSettingsDialogFragment()
                .apply {
                    arguments = Bundle().apply { putString(FOLDER, Gson().toJson(folderModel)) }
                }
    }

    private lateinit var folderModel: FolderModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val json = requireArguments().get(FOLDER) as String
        folderModel =
            Gson().fromJson(json, FolderModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FolderSettingsFragmentBinding.inflate(inflater, container, false)
        initialize(binding)
        return binding.root
    }

    private fun initialize(binding: FolderSettingsFragmentBinding) {
        settingsViewModel.currentTheme.observe(viewLifecycleOwner, { theme ->
            val cardViewTextColor = theme.drawerTextFillColor
            val backgroundColor = theme.drawerSearchBackgroundColor
            val cardViewBackgroundColor = theme.drawerBackgroundColor
            val textFillColor = theme.drawerTextFillColor

            binding.apply {
                folderSettings.setTextColor(cardViewTextColor)
                changeTitleText.setTextColor(cardViewTextColor)
                constraintLayout.setBackgroundColor(backgroundColor)
                cardView.setCardBackgroundColor(cardViewBackgroundColor)
                cardView.elevation = 18f
                backgroundColorText.setTextColor(textFillColor)
                textColorText.setTextColor(textFillColor)
                changeTitleText.setTextColor(textFillColor)
                removeFolderText.setTextColor(textFillColor)
                editApps.setTextColor(textFillColor)
            }
        })


        Timber.d("Initializing dialog")
        binding.backgroundColorCircle.also { backgroundCircle ->
            backgroundCircle.setColorFilter(folderModel.backgroundColor)
            backgroundCircle.setOnClickListener {
                ChromaDialog.Builder()
                    .initialColor(folderModel.backgroundColor)
                    .colorMode(ColorMode.ARGB)
                    .onColorSelected(object : ColorSelectListener {
                        override fun onColorSelected(color: Int) {
                            backgroundCircle.setColorFilter(color)
                            folderModel.backgroundColor = color
                            homescreenViewModel.updateFolder(folderModel)
                        }

                    })
                    .create()
                    .show(parentFragmentManager, "")
            }
        }

        binding.textColorCircle.also { textColorCircle ->
            DrawableCompat.wrap(textColorCircle.drawable).apply {
                setTint(folderModel.itemColor)
            }
            textColorCircle.imageTintList = ColorStateList.valueOf(SEMITRANSPARENT_STROKE_COLOR)
            textColorCircle.setOnClickListener {
                ChromaDialog.Builder()
                    .initialColor(folderModel.backgroundColor)
                    .colorMode(ColorMode.HSV)
                    .onColorSelected(object : ColorSelectListener {
                        override fun onColorSelected(color: Int) {
                            textColorCircle.setColorFilter(color)
                            folderModel.itemColor = color
                            homescreenViewModel.updateFolder(folderModel)
                        }
                    })
                    .create()
                    .show(parentFragmentManager, "")
            }
        }

        binding.changeTitleText.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Change Folder Name")

            val editText = EditText(context)
            editText.setText(folderModel.title)
            editText.inputType = InputType.TYPE_CLASS_TEXT

            builder.apply {
                setView(editText)
                setPositiveButton("Confirm") { _, _ ->
                    folderModel.title = editText.text.toString()
                    homescreenViewModel.updateFolder(folderModel)
                }
                setNegativeButton("Cancel") { dialog, _ ->
                    dialog.cancel()
                }

                show()
            }
        }
        Timber.d("Dialog initialized")

        binding.removeFolderText.setOnClickListener {
            homescreenViewModel.deleteFolder(folderModel)
            dismiss()
        }

        binding.editApps.setOnClickListener {
            EditFolderItemsDialogFragment.newInstance(folderModel.id!!)
                .show(requireActivity().supportFragmentManager, "editFolderItemsDialog")
        }
    }
}
