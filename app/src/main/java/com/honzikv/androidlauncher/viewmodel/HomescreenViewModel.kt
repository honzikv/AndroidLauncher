package com.honzikv.androidlauncher.viewmodel

import android.content.pm.PackageManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.honzikv.androidlauncher.model.*
import com.honzikv.androidlauncher.repository.HomescreenRepository
import com.honzikv.androidlauncher.utils.BackgroundTransformations
import com.honzikv.androidlauncher.utils.Event
import com.honzikv.androidlauncher.utils.IntegrityException
import com.honzikv.androidlauncher.utils.MAX_ITEMS_IN_FOLDER
import kotlinx.coroutines.launch
import timber.log.Timber

class HomescreenViewModel(
    private val homescreenRepository: HomescreenRepository,
    private val packageManager: PackageManager
) : ViewModel() {

    companion object {
        const val FOLDER_FULL_NO_ITEMS_ADDED =
            "Folder is full, nothing was added from selected apps"
        const val FOLDER_FULL_SOMETHING_ADDED = "Folder is full, not all items were added"
    }

    private val folderPostError = MutableLiveData<Event<String>>()
    fun getFolderPostError() = folderPostError as LiveData<Event<String>>

    private val pageDeleteError = MutableLiveData<Event<String>>()
    fun getPageDeleteError() = pageDeleteError as LiveData<Event<String>>

    /**
     * K LiveData objektu obsahujiciho vsechny stranky se slozkami navic nacte i ikony a popisky
     * do noveho LiveData objektu - protoze vychozi Transformations nabizi pouze transformaci na
     * hlavnim (UI) vlakne pouzil jsem implementaci ze stack overflow, ktera vse provede na vedlejsim
     * a vysledek posle pomoci postValue metody
     */
    val allPagesWithFolders =
        BackgroundTransformations.map(homescreenRepository.allPagesWithFolders) { pageList ->
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

    /**
     * Aktualizuje slozku ve vedlejsim vlakne
     */
    fun updateFolder(folderModel: FolderModel) = viewModelScope.launch {
        homescreenRepository.updateFolder(folderModel)
    }

    /**
     * Smaze slozku s [folderId] ve vedlejsim vlakne
     */
    fun deleteFolder(folderId: Long) = viewModelScope.launch {
        homescreenRepository.deleteFolder(folderId)
    }

    /**
     * Smaze stranku s [pageId] ve vedlejsim vlakne, pokud je posledni posle zpravu do [pageDeleteError]
     */
    fun deletePage(pageId: Long) = viewModelScope.launch {
        try {
            homescreenRepository.removePage(pageId)
        } catch (exception: IntegrityException) {
            pageDeleteError.postValue(
                Event(
                    exception.message!!
                )
            )
        }
    }

    /**
     * Prida novou stranku do databaze ve vedlejsim vlakne. Pokud je [addAsFirst] prida ji na zacatek,
     * jinak ji prida na konec
     */
    fun addPage(addAsFirst: Boolean) = viewModelScope.launch {
        if (!addAsFirst) {
            homescreenRepository.addPageAsLast()
        } else {
            homescreenRepository.addPageAsFirst()
        }
    }

    /**
     * Prida slozku do databaze a pripoji ji ke strance. Funkce se provede ve vedlejsim vlakne
     */
    fun addFolderToPage(folderModel: FolderModel, pageId: Long) = viewModelScope.launch {
        val folderId = homescreenRepository.addFolderWithoutPage(folderModel)
        homescreenRepository.addFolderToPage(folderId, pageId)
    }

    /**
     * Vrati LiveData se strankou a jejimi slozkami
     */
    fun getPageWithFolders(pageId: Long): LiveData<PageWithFolders> =
        homescreenRepository.getPageWithFolders(pageId)

    /**
     * Smaze slozku s id [folderId] ve vedlejsim vlakne
     */
    fun deleteFolderWithId(folderId: Long) = viewModelScope.launch {
        homescreenRepository.deleteFolderWithId(folderId)
    }

    /**
     * Ve vedlejsim vlakne prida vybrane aplikace ze [selectedApps] do slozky s id [folderId].
     * Prida pouze aplikace, ktere ve slozce jeste nejsou.
     */
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

            //filtrovani duplicitnich slozek
            selectedApps.forEach { app ->
                if (!items.any { it.packageName == app.packageName }) {
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
                val addCount = MAX_ITEMS_IN_FOLDER - items.size
                if (addCount == 0) {
                    folderPostError.postValue(
                        Event(
                            FOLDER_FULL_NO_ITEMS_ADDED
                        )
                    )
                    return@launch
                }

                newItems = newItems.subList(0, addCount)
                folderPostError.postValue(
                    Event(
                        FOLDER_FULL_SOMETHING_ADDED
                    )
                )
            }

            //Pridani aplikaci do slozky
            homescreenRepository.addItemsWithFolderId(newItems)
        }

    /**
     * Vrati LiveData objekt obsahujici [FolderWithItems] se slozkou s id [folderId]
     */
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

    /**
     * Smaze slozku s id [id] ve vedlejsim vlakne
     */
    fun deleteFolderItem(id: Long) = viewModelScope.launch {
        homescreenRepository.deleteFolderItem(id)
    }

    /**
     * Vrati prvni stranku. Potreba spustit v coroutine
     */
    suspend fun getFirstPage() = homescreenRepository.getFirstPage()

    /**
     * Prida stranku na zacatek. Potreba spustit v coroutine
     */
    suspend fun addPageSuspend() = homescreenRepository.addPageAsFirst()

    /**
     * Prida slozku do databaze. Potreba spustit v coroutine
     */
    suspend fun addFolderSuspend(folderModel: FolderModel) =
        homescreenRepository.addFolderWithoutPage(folderModel)

    /**
     * Prida aplikace do slozky. Potreba spustit v coroutine
     */
    suspend fun addItems(items: List<FolderItemModel>) {
        homescreenRepository.addItemsWithFolderId(items)
    }

    /**
     * Aktualizuje seznam slozek ve vedlejsim vlakne
     */
    fun updateFolderList(itemList: List<FolderModel>) = viewModelScope.launch {
        homescreenRepository.updateFolderList(itemList)
    }

    /**
     * Aktualizuje seznam aplikaci ve slozce ve vedlejsim vlakne
     */
    fun updateFolderItemList(itemList: List<FolderItemModel>) = viewModelScope.launch {
        homescreenRepository.updateFolderItemList(itemList)
    }

    /**
     * Aktualizuje seznam stranek ve vedlejsim vlakne.
     */
    fun updatePageList(itemList: List<PageModel>) = viewModelScope.launch {
        homescreenRepository.updatePageList(itemList)
    }
}