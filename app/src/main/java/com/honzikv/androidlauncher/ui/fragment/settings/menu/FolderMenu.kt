package com.honzikv.androidlauncher.ui.fragment.settings.menu

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.honzikv.androidlauncher.ui.fragment.settings.adapter.HeaderItem
import com.honzikv.androidlauncher.ui.fragment.settings.adapter.TextLeftRightItem
import com.honzikv.androidlauncher.viewmodel.SettingsViewModel
import com.multilevelview.models.RecyclerViewItem

class FolderMenu(viewModel: SettingsViewModel, context: Context) {

    var position = -1

    companion object {
        const val FOLDER_SETTINGS = "Folder Settings"
        const val FOLDER_SETTINGS_SUB = "Folder Look Options"

    }

    private val folderSettings = HeaderItem(FOLDER_SETTINGS, FOLDER_SETTINGS_SUB, 0)

    fun getRoot() = folderSettings as RecyclerViewItem
}