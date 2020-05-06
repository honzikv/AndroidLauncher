package com.honzikv.androidlauncher.viewmodel

import android.content.pm.PackageManager
import androidx.lifecycle.*
import com.honzikv.androidlauncher.data.model.FolderModel
import com.honzikv.androidlauncher.data.model.FolderWithItems
import com.honzikv.androidlauncher.data.model.PageModel
import com.honzikv.androidlauncher.data.repository.HomescreenRepository
import com.honzikv.androidlauncher.transformation.BackgroundTransformations
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent

class HomescreenViewModel(
    private val homescreenRepository: HomescreenRepository,
    private val packageManager: PackageManager
) : ViewModel(), KoinComponent {

    private val currentPageNumber: MutableLiveData<Int> = MutableLiveData(0)

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

}