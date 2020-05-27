package com.honzikv.androidlauncher.ui.fragment.picker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager

import com.honzikv.androidlauncher.databinding.AppPickerDialogFragmentBinding
import com.honzikv.androidlauncher.ui.fragment.picker.adapter.AppPickerAdapter
import com.honzikv.androidlauncher.viewmodel.DrawerViewModel
import com.honzikv.androidlauncher.viewmodel.DockViewModel
import com.honzikv.androidlauncher.viewmodel.HomescreenViewModel
import com.honzikv.androidlauncher.viewmodel.SettingsViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel
import timber.log.Timber
import kotlin.properties.Delegates

class AppPickerDialogFragment private constructor() : DialogFragment() {

    companion object {
        private const val CONTAINER_ID = "containerId"
        fun getDockFolderId() = -1L
        fun newInstance(folderId: Long) =
            AppPickerDialogFragment().apply {
                arguments = Bundle().apply { putLong(CONTAINER_ID, folderId) }
            }
    }

    private val settingsViewModel: SettingsViewModel by sharedViewModel()

    private val drawerViewModel: DrawerViewModel by sharedViewModel()

    private val homescreenViewModel: HomescreenViewModel by sharedViewModel()

    private val dockViewModel: DockViewModel by sharedViewModel()

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

        appPickerAdapter.getSelectedItemsCount().observe(viewLifecycleOwner) {
            binding.appsSelected.text = "Apps selected: $it"
        }

        settingsViewModel.currentTheme.observe(viewLifecycleOwner, { theme ->
            val backgroundColor = theme.drawerSearchBackgroundColor
            val cardViewBackgroundColor = theme.drawerBackgroundColor
            val textFillColor = theme.drawerTextFillColor

            binding.apply {
                constraintLayout.setBackgroundColor(backgroundColor)
                cardView.setCardBackgroundColor(cardViewBackgroundColor)
                okButton.setColorFilter(textFillColor)
                chooseApps.setTextColor(textFillColor)
                appPickerAdapter.setTheme(theme)
                appPickerAdapter.notifyDataSetChanged()
                appsSelected.setTextColor(textFillColor)
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
            binding.okButton.setOnClickListener {
                val selectedApps = appPickerAdapter.getSelectedItems()
                selectedApps.forEach {
                    /*
                    Nastavime zaskrtnuti na false, jinak by se pri pristim spusteni dialogu mohlo
                    stat, ze budou zaskrtnuty znovu
                     */
                    it.isChecked = false
                }
                homescreenViewModel.addItemsToFolder(folderId, selectedApps)
                dismiss()
            }
        } else {
            binding.okButton.setOnClickListener {
                val selectedApps = appPickerAdapter.getSelectedItems()
                selectedApps.forEach {
                    it.isChecked = false
                }
                dockViewModel.addItemsToDock(selectedApps)
                dismiss()
            }
        }
    }

}
