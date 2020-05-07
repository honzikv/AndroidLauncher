package com.honzikv.androidlauncher.viewmodel

import android.content.pm.PackageManager
import androidx.lifecycle.*
import com.honzikv.androidlauncher.data.model.FolderModel
import com.honzikv.androidlauncher.data.model.PageModel
import com.honzikv.androidlauncher.data.repository.HomescreenRepository
import kotlinx.coroutines.launch

class HomescreenViewModel(
    private val homescreenRepository: HomescreenRepository,
    private val packageManager: PackageManager
) : ViewModel() {

    val allPages = Transformations.map(homescreenRepository.allPages) { pageList ->
        return@map pageList.apply {
            forEach { pageWithFolders ->
                pageWithFolders.folderList.forEach { folderWithItems ->
                    folderWithItems.itemList.forEach { item ->
                        val info = packageManager.getApplicationInfo(item.systemAppPackageName, 0)
                        item.label = info.loadLabel(packageManager).toString()
                        item.icon = info.loadIcon(packageManager)
                    }
                }
            }
        }
    }


    fun updateFolder(folderModel: FolderModel) {
        viewModelScope.launch {
            homescreenRepository.updateFolder(folderModel)
        }
    }

    fun deleteFolder(folderModel: FolderModel) {
        viewModelScope.launch {
            homescreenRepository.deleteFolder(folderModel)
        }
    }

    fun deletePage(pageModel: PageModel) {
        viewModelScope.launch {
            homescreenRepository.deletePage(pageModel)
        }
    }

    fun addPage(addFirst: Boolean) {
        viewModelScope.launch {
            if (!addFirst) {
                homescreenRepository.addPageAsLast()
            }
            else {
                homescreenRepository.addPageAsFirst()
            }
        }
    }

}