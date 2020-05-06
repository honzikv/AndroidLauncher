package com.honzikv.androidlauncher.ui.fragment.dialog

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.graphics.drawable.DrawableCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson

import com.honzikv.androidlauncher.data.model.FolderModel
import com.honzikv.androidlauncher.databinding.FolderSettingsFragmentBinding
import com.honzikv.androidlauncher.viewmodel.HomescreenViewModel
import me.priyesh.chroma.ChromaDialog
import me.priyesh.chroma.ColorMode
import me.priyesh.chroma.ColorSelectListener
import org.koin.android.ext.android.inject
import timber.log.Timber

class FolderSettingsDialogFragment : BottomSheetDialogFragment() {

    private val homescreenViewModel: HomescreenViewModel by inject()

    companion object {
        const val FOLDER = "folder"

        /**
         * [bundle] containing serialized folderModel
         */
        fun newInstance(bundle: Bundle) =
            FolderSettingsDialogFragment()
                .apply {
                arguments = bundle
                Timber.d("Creating new instance of FolderSettingsFragment\n passed folder = ${bundle.get(
                    FOLDER
                )}")
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
        Timber.d("Initializing dialog")
        binding.backgroundColorCircle.also { backgroundCircle ->
            DrawableCompat.wrap(backgroundCircle.drawable).setTint(folderModel.backgroundColor)
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
            DrawableCompat.wrap(textColorCircle.drawable).setTint(folderModel.itemColor)
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
    }
}
