package com.honzikv.androidlauncher.ui.fragment.dialog

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.LiveData
import com.honzikv.androidlauncher.data.model.FolderModel
import com.honzikv.androidlauncher.ui.fragment.dialog.adapter.EditFolderAdapter
import com.honzikv.androidlauncher.viewmodel.HomescreenViewModel
import com.honzikv.androidlauncher.viewmodel.SettingsViewModel
import org.koin.android.ext.android.inject

class EditFolderItemsDialogFragment : DialogFragment() {

    private val homescreenViewModel: HomescreenViewModel by inject()

    private val settingsViewModel: SettingsViewModel by inject()

    private lateinit var folder: LiveData<FolderModel>

    private lateinit var folderAdapter: EditFolderAdapter
}