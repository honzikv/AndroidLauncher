package com.honzikv.androidlauncher.ui.fragment.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.observe
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager

import com.honzikv.androidlauncher.databinding.AppPickerDialogFragmentBinding
import com.honzikv.androidlauncher.exception.DockIsFullException
import com.honzikv.androidlauncher.ui.fragment.dialog.adapter.AppPickerAdapter
import com.honzikv.androidlauncher.viewmodel.DrawerViewModel
import com.honzikv.androidlauncher.viewmodel.DockViewModel
import com.honzikv.androidlauncher.viewmodel.HomescreenViewModel
import com.honzikv.androidlauncher.viewmodel.SettingsViewModel
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import timber.log.Timber
import kotlin.properties.Delegates

class AppPickerDialogFragment : DialogFragment() {

    companion object {
        private const val CONTAINER_ID = "containerId"
        fun getDockFolderId() = -1L
        fun newInstance(folderId: Long) =
            AppPickerDialogFragment().apply {
                arguments = Bundle().apply { putLong(CONTAINER_ID, folderId) }
            }
    }

    private val settingsViewModel: SettingsViewModel by inject()

    private val drawerViewModel: DrawerViewModel by inject()

    private lateinit var homescreenViewModel: HomescreenViewModel

    private lateinit var dockViewModel: DockViewModel

    private var folderId by Delegates.notNull<Long>()

    private lateinit var appPickerAdapter: AppPickerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, android.R.style.ThemeOverlay_Material_Dark);
        folderId = requireArguments()[CONTAINER_ID] as Long
        Timber.d("Folder id is $folderId")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = AppPickerDialogFragmentBinding.inflate(layoutInflater)
        initialize(binding)
        return binding.root
    }

    private fun initialize(binding: AppPickerDialogFragmentBinding) {

        appPickerAdapter = AppPickerAdapter()

        settingsViewModel.currentTheme.observe(viewLifecycleOwner, { theme ->
            val backgroundColor = theme.drawerSearchBackgroundColor
            val cardViewBackgroundColor = theme.drawerBackgroundColor
            val textFillColor = theme.drawerTextFillColor

            binding.apply {
                constraintLayout.setBackgroundColor(backgroundColor)
                cardView.setCardBackgroundColor(cardViewBackgroundColor)
                okButton.setColorFilter(textFillColor)
                chooseApps.setTextColor(textFillColor)
                appPickerAdapter.setTextColor(textFillColor)
                appPickerAdapter.notifyDataSetChanged()
            }
        })

        drawerViewModel.getDrawerApps().observe(viewLifecycleOwner, { items ->
            appPickerAdapter.setItems(items)
            appPickerAdapter.notifyDataSetChanged()
        })

        binding.recyclerView.apply {
            adapter = appPickerAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }


        //Dock has id of -1 (which is never used by SQLite DB in Folder table)
        if (folderId != getDockFolderId()) {
            //inject only neccessary viewmodel
            homescreenViewModel = get()
            binding.okButton.setOnClickListener {
                val selectedApps = appPickerAdapter.getSelectedItems()
                selectedApps.forEach { Timber.d("$it") }
                homescreenViewModel.addItemsToFolder(folderId, selectedApps)
                dismiss()
            }
        } else {
            dockViewModel = get()
            binding.okButton.setOnClickListener {
                val selectedApps = appPickerAdapter.getSelectedItems()
                try {
                    dockViewModel.addItemsToDock(selectedApps)
                } catch (ex: DockIsFullException) {
                    Toast.makeText(requireContext(), ex.message, Toast.LENGTH_LONG).show()
                }
                dismiss()
            }
        }
    }

}
