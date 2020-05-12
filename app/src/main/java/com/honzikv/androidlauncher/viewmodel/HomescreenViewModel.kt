package com.honzikv.androidlauncher.viewmodel

import android.content.pm.PackageManager
import androidx.lifecycle.*
import com.honzikv.androidlauncher.MAX_ITEMS_IN_FOLDER
import com.honzikv.androidlauncher.data.model.*
import com.honzikv.androidlauncher.data.repository.HomescreenRepository
import com.honzikv.androidlauncher.transformation.BackgroundTransformations
import com.honzikv.androidlauncher.ui.callback.Event
import kotlinx.coroutines.launch
import timber.log.Timber

class HomescreenViewModel(
    private val homescreenRepository: HomescreenRepository,
    private val packageManager: PackageManager
) : ViewModel() {

    private val folderPostErrorMutable = MutableLiveData<Event<String>>()
    val folderPostError: LiveData<Event<String>> get() = folderPostErrorMutable

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

    fun addPage(addAsFirst: Boolean) = viewModelScope.launch {
        if (!addAsFirst) {
            homescreenRepository.addPageAsLast()
        } else {
            homescreenRepository.addPageAsFirst()
        }
    }

    fun addFolderToPage(folderModel: FolderModel, pageModel: PageModel) = viewModelScope.launch {
        Timber.d("adding folder to page")
        val folderId = homescreenRepository.addFolder(folderModel)
        Timber.d("page id = ${pageModel.id}")
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
            val items = folderWithItems.itemList

            var lastPosition = if (items.isEmpty()) {
                -1
            } else {
                items.maxBy { it.position }!!.position
            }
            var newItems = mutableListOf<FolderItemModel>()

            //filter duplicates
            selectedApps.forEach { app ->
                if (!items.any { it.packageName == app.packageName }) {
                    Timber.d("This app is unique, adding")
                    lastPosition += 1
                    newItems.add(
                        FolderItemModel(
                            folderId = folderWithItems.folder.id,
                            packageName = app.packageName,
                            position = lastPosition
                        )
                    )
                }
            }

            if (items.size + newItems.size > MAX_ITEMS_IN_FOLDER) {
                Timber.d("folder limit reached")
                val addCount = MAX_ITEMS_IN_FOLDER - items.size
                if (addCount == 0) {
                    TODO()
                    return@launch
                }

                //set [newItems] pointer to sublist
                newItems = newItems.subList(0, addCount)
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

    suspend fun getFirstPage() = homescreenRepository.getFirstPage()

    suspend fun addPageSuspend() = homescreenRepository.addPageAsFirst()

    suspend fun addFolderSuspend(folderModel: FolderModel) =
        homescreenRepository.addFolderWithoutPage(folderModel)

    suspend fun addItems(items: List<FolderItemModel>) {
        homescreenRepository.addItemsWithFolderId(items)
    }
}