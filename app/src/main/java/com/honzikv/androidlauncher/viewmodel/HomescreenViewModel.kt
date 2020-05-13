package com.honzikv.androidlauncher.viewmodel

import android.content.pm.PackageManager
import androidx.lifecycle.*
import com.honzikv.androidlauncher.util.MAX_ITEMS_IN_FOLDER
import com.honzikv.androidlauncher.model.*
import com.honzikv.androidlauncher.repository.HomescreenRepository
import com.honzikv.androidlauncher.util.BackgroundTransformations
import com.honzikv.androidlauncher.util.callback.Event
import kotlinx.coroutines.launch
import timber.log.Timber

class HomescreenViewModel(
    private val homescreenRepository: HomescreenRepository,
    private val packageManager: PackageManager
) : ViewModel() {

    private val folderPostErrorMutable = MutableLiveData<Event<String>>()
    val folderPostError: LiveData<Event<String>> get() = folderPostErrorMutable

    override fun onCleared() {
        Timber.d("clearing viewmodel")
        super.onCleared()
    }

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

    fun deleteFolder(folderId: Long) = viewModelScope.launch {
        homescreenRepository.deleteFolder(folderId)
    }

    fun deletePage(pageId: Long) = viewModelScope.launch {
        homescreenRepository.removePage(pageId)
    }

    fun addPage(addAsFirst: Boolean) = viewModelScope.launch {
        if (!addAsFirst) {
            homescreenRepository.addPageAsLast()
        } else {
            homescreenRepository.addPageAsFirst()
        }
    }

    /**
     * Prida slozku do databaze a pripoji ji ke strance
     */
    fun addFolderToPage(folderModel: FolderModel, pageId: Long) = viewModelScope.launch {
        val folderId = homescreenRepository.addFolderWithoutPage(folderModel)
        homescreenRepository.addFolderToPage(folderId, pageId)
    }

    fun getPageWithFolders(pageId: Long): LiveData<PageWithFolders> =
        homescreenRepository.getPageWithFolders(pageId)

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

    suspend fun getFirstPage() = homescreenRepository.getFirstPage()

    suspend fun addPageSuspend() = homescreenRepository.addPageAsFirst()

    suspend fun addFolderSuspend(folderModel: FolderModel) =
        homescreenRepository.addFolderWithoutPage(folderModel)

    suspend fun addItems(items: List<FolderItemModel>) {
        homescreenRepository.addItemsWithFolderId(items)
    }
}