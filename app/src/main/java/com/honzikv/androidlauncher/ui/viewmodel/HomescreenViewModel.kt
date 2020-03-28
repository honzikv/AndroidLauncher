package com.honzikv.androidlauncher.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.honzikv.androidlauncher.data.model.DrawerAppModel
import com.honzikv.androidlauncher.data.repository.DockDataRepository
import com.honzikv.androidlauncher.data.repository.FolderDataRepository
import com.honzikv.androidlauncher.data.repository.HomescreenPageRepository
import com.honzikv.androidlauncher.data.repository.SystemAppsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Homescreen fragment viewmodel
 */
class HomescreenViewModel(
    val dockDataRepository: DockDataRepository,
    val homescreenPageRepository: HomescreenPageRepository,
    val systemAppsRepository: SystemAppsRepository,
    val folderDataRepository: FolderDataRepository
) : ViewModel() {

    private var currentPage = 0

    private lateinit var  drawerAppList: LiveData<List<DrawerAppModel>>

    fun getDrawerAppList() : LiveData<List<DrawerAppModel>> {
        if (!::drawerAppList.isInitialized) {
            drawerAppList = LiveData()
        }
    }

    fun getFolders(page: Int) {

    }

}