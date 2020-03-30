package com.honzikv.androidlauncher.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.honzikv.androidlauncher.data.repository.DockDataRepository
import com.honzikv.androidlauncher.data.repository.FolderDataRepository
import com.honzikv.androidlauncher.data.repository.HomescreenPageRepository

/**
 * Homescreen fragment viewmodel
 */
class HomescreenViewModel(
    val dockDataDataRepository: DockDataRepository,
    val homescreenPageRepository: HomescreenPageRepository,
    val folderDataRepository: FolderDataRepository
) : ViewModel() {

    private var currentPage = 0
    get



}