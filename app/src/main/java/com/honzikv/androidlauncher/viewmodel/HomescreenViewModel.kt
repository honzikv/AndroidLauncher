package com.honzikv.androidlauncher.viewmodel

import android.content.pm.PackageManager
import androidx.lifecycle.*
import com.honzikv.androidlauncher.data.model.*
import com.honzikv.androidlauncher.data.repository.HomescreenRepository
import com.honzikv.androidlauncher.transformation.BackgroundTransformations
import kotlinx.coroutines.launch
import timber.log.Timber

class HomescreenViewModel(
    private val homescreenRepository: HomescreenRepository,
    private val packageManager: PackageManager
) : ViewModel() {

    val allPages = BackgroundTransformations.map(homescreenRepository.allPages) { pageList ->
        return@map pageList.apply {
            forEach { pageWithFolders ->
                pageWithFolders.folderList.forEach { folderWithItems ->
                    folderWithItems.itemList.forEach { item ->
                        val info = packageManager.getApplicationInfo(item.packageName, 0)
                        item.label = info.loadLabel(packageManager).toString()
                        item.icon = info.loadIcon(packageManager)
                    }
                }
            }
        }
    }


    fun updateFolder(folderModel: FolderModel) = viewModelScope.launch {
        homescreenRepository.updateFolder(folderModel)
    }

    fun deleteFolder(folderModel: FolderModel) = viewModelScope.launch {
        homescreenRepository.deleteFolder(folderModel)
    }

    fun deletePage(pageModel: PageModel) = viewModelScope.launch {
        homescreenRepository.deletePage(pageModel)
    }

    fun addPage(addFirst: Boolean) = viewModelScope.launch {
        if (!addFirst) {
            homescreenRepository.addPageAsLast()
        } else {
            homescreenRepository.addPageAsFirst()
        }
    }

    fun addFolderToPage(folderModel: FolderModel, pageModel: PageModel) = viewModelScope.launch {
        val folderId = homescreenRepository.addFolder(folderModel)
        homescreenRepository.addFolderToPage(folderId, pageModel.id!!)
    }

    fun getPageWithFolders(pageId: Long): LiveData<PageWithFolders> =
        homescreenRepository.getPageWithFolders(pageId)

    fun deletePageWithId(pageId: Long) = viewModelScope.launch {
        homescreenRepository.deletePageWithId(pageId)
    }

    fun deleteFolderWithId(folderId: Long) = viewModelScope.launch {
        homescreenRepository.deleteFolderWithId(folderId)
    }

    fun swapFolderPositions(folder1: FolderModel, folder2: FolderModel) = viewModelScope.launch {
        val swap = folder1.position
        folder1.position = folder2.position
        folder2.position = swap
        homescreenRepository.updateFolders(folder1, folder2)
    }

    fun swapFolderItemsPositions(item1: FolderItemModel, item2: FolderItemModel) =
        viewModelScope.launch {
            val swap = item1.position
            item1.position = item2.position
            item2.position = swap
            homescreenRepository.updateFolderItems(item1, item2)
        }

    fun updateFolders(vararg folders: FolderModel) = viewModelScope.launch {
        homescreenRepository.updateFolders(*folders)
    }

    fun addItemsToFolder(folderId: Long, selectedApps: MutableList<DrawerApp>) =
        viewModelScope.launch {
            Timber.d("Adding items to folder")
            val folderWithItems = homescreenRepository.getFolderWithItems(folderId)
            val newItems = mutableListOf<FolderItemModel>()

            selectedApps.forEach { app ->
                //Check if collection contains element with same package name, if it doesnt add the item
                if (!folderWithItems.itemList.any { it.packageName == app.packageName }) {
                    Timber.d("This app is unique, adding")
                    newItems.add(
                        FolderItemModel(
                            folderId = folderWithItems.folder.id,
                            packageName = app.packageName
                        )
                    )
                }
            }

            //Add new items to the database
            homescreenRepository.addItemsWithFolderId(newItems)
        }

    fun getFolderWithItemsLiveData(folderId: Long) =
        BackgroundTransformations.map(
            homescreenRepository.getFolderWithItemsLiveData(folderId)
        ) { folderWithItems ->
            return@map folderWithItems.apply {
                itemList.forEach { item ->
                    val info = packageManager.getApplicationInfo(item.packageName, 0)
                    item.label = info.loadLabel(packageManager).toString()
                    item.icon = info.loadIcon(packageManager)
                }
            }
        }


    fun deleteFolderItem(id: Long) = viewModelScope.launch {
        homescreenRepository.deleteFolderItem(id)
    }

    fun getFolderLiveData(folderId: Long): LiveData<FolderModel> =
        homescreenRepository.getFolderLiveData(folderId)
}