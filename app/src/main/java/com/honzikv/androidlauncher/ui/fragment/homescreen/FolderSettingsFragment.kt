package com.honzikv.androidlauncher.ui.fragment.homescreen

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.google.gson.Gson

import com.honzikv.androidlauncher.data.model.FolderModel
import com.honzikv.androidlauncher.databinding.FolderSettingsFragmentBinding
import com.honzikv.androidlauncher.viewmodel.FolderSettingsViewModel
import me.priyesh.chroma.ChromaDialog
import me.priyesh.chroma.ColorMode
import me.priyesh.chroma.ColorSelectListener
import org.koin.android.ext.android.inject
import timber.log.Timber

class FolderSettingsFragment() : Fragment() {

    private val folderSettingsViewModel: FolderSettingsViewModel by inject()

    companion object {
        const val FOLDER = "folder"

        /**
         * [bundle] containing serialized folderModel
         */
        fun newInstance(bundle: Bundle) =
            DialogFragment().apply {
                arguments = bundle
            }.also {
                Timber.d("Creating new instance of FolderSettingsFragment")
            }
    }

    private lateinit var folderModel: FolderModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        folderModel =
            Gson().fromJson(savedInstanceState?.get(FOLDER) as String, FolderModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FolderSettingsFragmentBinding.inflate(inflater)
        initialize(binding)
        return binding.root
    }

    private fun initialize(binding: FolderSettingsFragmentBinding) {
        Timber.d("Initializing dialog")
        binding.backgroundColorCircle.also { backgroundCircle ->
            backgroundCircle.setBackgroundColor(folderModel.backgroundColor)
            backgroundCircle.setOnClickListener {
                ChromaDialog.Builder()
                    .initialColor(folderModel.backgroundColor)
                    .colorMode(ColorMode.ARGB)
                    .onColorSelected(object : ColorSelectListener {
                        override fun onColorSelected(color: Int) {
                            backgroundCircle.setBackgroundColor(color)
                            folderModel.backgroundColor = color
                            folderSettingsViewModel.updateFolder(folderModel)
                        }

                    })
                    .create()
                    .show(parentFragmentManager, "")
            }
        }

        binding.textColorCircle.also { textColorCircle ->
            textColorCircle.setBackgroundColor(folderModel.itemColor)
            textColorCircle.setOnClickListener {
                ChromaDialog.Builder()
                    .initialColor(folderModel.backgroundColor)
                    .colorMode(ColorMode.RGB)
                    .onColorSelected(object : ColorSelectListener {
                        override fun onColorSelected(color: Int) {
                            textColorCircle.setBackgroundColor(color)
                            folderModel.itemColor = color
                            folderSettingsViewModel.updateFolder(folderModel)
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
                    folderSettingsViewModel.updateFolder(folderModel)
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
