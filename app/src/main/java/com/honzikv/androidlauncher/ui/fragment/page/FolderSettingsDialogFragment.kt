package com.honzikv.androidlauncher.ui.fragment.page

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.observe
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.honzikv.androidlauncher.databinding.FolderSettingsFragmentBinding
import com.honzikv.androidlauncher.model.FolderModel
import com.honzikv.androidlauncher.viewmodel.HomescreenViewModel
import com.honzikv.androidlauncher.viewmodel.SettingsViewModel
import me.priyesh.chroma.ChromaDialog
import me.priyesh.chroma.ColorMode
import me.priyesh.chroma.ColorSelectListener
import org.koin.android.viewmodel.ext.android.sharedViewModel

/**
 * Dialog s nastavenim slozky - jedna se o male okno, ktere se zobrazi ve spodu obrazovky
 * - bottom sheet
 */
class FolderSettingsDialogFragment private constructor() : BottomSheetDialogFragment() {

    private val homescreenViewModel: HomescreenViewModel by sharedViewModel()

    private val settingsViewModel: SettingsViewModel by sharedViewModel()

    companion object {
        const val FOLDER = "folder"

        /**
         * [folderModel] - slozka, ktera se bude upravovat
         */
        fun newInstance(folderModel: FolderModel) =
            FolderSettingsDialogFragment()
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

    /**
     * Inicializace UI
     */
    private fun initialize(binding: FolderSettingsFragmentBinding) {
        //Zmena barev UI podle tematu
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
                backgroundColorText.setTextColor(textFillColor)
                textColorText.setTextColor(textFillColor)
                changeTitleText.setTextColor(textFillColor)
                removeFolderText.setTextColor(textFillColor)
                editApps.setTextColor(textFillColor)
            }
        })

        //Ikona pro nastaveni pozadi slozky, ktera spusti dialog s color pickerem
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

        //Ikona pro nastaveni barvy textu slozky, ktera spusti dialog s color pickerem
        binding.textColorCircle.also { textColorCircle ->
            textColorCircle.setColorFilter(folderModel.itemColor)
            textColorCircle.setOnClickListener {
                ChromaDialog.Builder()
                    .initialColor(folderModel.itemColor)
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

        //Nastavi upravu nazvu - spusti edit text dialog
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
                setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
                show()
            }
        }

        //Smazani folderu a ukonceni dilaogu
        binding.removeFolderText.setOnClickListener {
            homescreenViewModel.deleteFolder(folderModel.id!!)
            dismiss()
        }

        //Spusteni dialogu pro upravu jednotlivych aplikaci ve slozce
        binding.editApps.setOnClickListener {
            EditFolderItemsDialogFragment.newInstance(folderModel.id!!)
                .show(requireActivity().supportFragmentManager, "editFolderItemsDialog")
        }
    }
}
