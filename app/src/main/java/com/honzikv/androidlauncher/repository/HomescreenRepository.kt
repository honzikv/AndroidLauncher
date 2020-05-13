package com.honzikv.androidlauncher.repository

import androidx.lifecycle.LiveData
import com.honzikv.androidlauncher.database.dao.FolderDao
import com.honzikv.androidlauncher.database.dao.PageDao
import com.honzikv.androidlauncher.model.FolderItemModel
import com.honzikv.androidlauncher.model.FolderModel
import com.honzikv.androidlauncher.model.PageModel
import com.honzikv.androidlauncher.model.PageWithFolders
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber


class HomescreenRepository(
    private val pageDao: PageDao,
    private val folderDao: FolderDao
) {

    val allPages: LiveData<List<PageWithFolders>> = pageDao.getAllPagesLiveData()

    /**
     * Adds new empty page to the database
     * @return id of added page
     */
    suspend fun addPageAsLast(): Long {
        val lastPage = pageDao.getLastPageNumber()
        return if (lastPage == null) {
            pageDao.addPage(PageModel(pageNumber = 0))
        } else {
            pageDao.addPage(PageModel(pageNumber = lastPage + 1))
        }
    }

    suspend fun addPageAsFirst(): Long {
        val pages = pageDao.getAllPages()
        Timber.d("pages size = ${pages.size}")
        pages.forEach { it.pageNumber = it.pageNumber + 1 }
        pageDao.updatePageList(pages)
        //PageModel has default value of position as 0
        return pageDao.addPage(PageModel())
    }

    suspend fun addFolderToPage(folderId: Long, pageId: Long) {
        val page = pageDao.getPage(pageId)
        val folder = folderDao.getFolder(folderId)

        Timber.d("folderPosition = ${page.nextFolderPosition}")
        Timber.d("page = $page")
        folder.position = page.nextFolderPosition
        folder.pageId = page.id
        page.nextFolderPosition = page.nextFolderPosition + 1

        Timber.d("updating page and folder")
        pageDao.updatePage(page)
        folderDao.updateFolder(folder)
    }

    suspend fun removePage(pageId: Long) {
        val pages = pageDao.getAllPages()
        var pageIndex = -1
        for (i in pages.indices) {
            if (pages[i].id == pageId) {
                pageIndex = i
                break
            }
        }

        if (pageIndex == -1) {
            return
        }

        val page = pages[pageIndex]
        for (i in pageIndex + 1 until pages.size) {
            pages[i].pageNumber = pages[i].pageNumber - 1
        }
        pages.removeAt(pageIndex)
        pageDao.updatePageList(pages)
        pageDao.deletePage(page)
    }

    suspend fun updateFolder(folderModel: FolderModel) = folderDao.updateFolder(folderModel)

    suspend fun deleteFolder(folderId: Long) {
        val folder = folderDao.getFolder(folderId)
        if (folder.pageId != null) {
            val pageWithFolders = pageDao.getPageWithFolders(folder.pageId!!)
            val folders = pageWithFolders.folderList

            var folderIndex = -1
            for (i in folders.indices) {
                if (folders[i].folder.id == folderId) {
                    folderIndex = i
                    break
                }
            }
            for (i in folderIndex + 1 until folders.size) {
                folders[i].folder.position = folders[i].folder.position!! - 1
                folderDao.updateFolder(folders[i].folder)
            }
        }
        folderDao.deleteFolderWithId(folderId)
    }

    suspend fun deleteFolderItem(folderItemId: Long) {
        val folderItem = folderDao.getFolderItem(folderItemId)
        if (folderItem.folderId == null) {
            val folder = folderDao.getFolderWithItems(folderItem.folderId!!)
            val items = folder.itemList

            var index = -1
            for (i in items.indices) {
                if (items[i].id == folderItemId) {
                    index = i
                    break
                }
            }
            for (i in index + 1 until items.size) {
                items[i].position = items[i].position - 1
                folderDao.updateFolderItem(items[i])
            }
        }
        folderDao.deleteFolderItem(folderItemId)
    }

    fun getPageWithFolders(pageId: Long): LiveData<PageWithFolders> =
        pageDao.getPageWithFoldersLiveData(pageId)

    suspend fun deleteFolderWithId(folderId: Long) = folderDao.deleteFolderWithId(folderId)

    suspend fun updateFolders(vararg folders: FolderModel) = folderDao.updateFolders(*folders)

    suspend fun updateFolderList(folderList: List<FolderModel>) = folderDao.updateFolderList(folderList)

    suspend fun getFolderWithItems(folderId: Long) = folderDao.getFolderWithItems(folderId)

    suspend fun addItemsWithFolderId(items: List<FolderItemModel>) =
        folderDao.insertItemsWithFolderId(items)

    fun getFolderWithItemsLiveData(folderId: Long) = folderDao.getFolderWithItemsLiveData(folderId)

    suspend fun updateFolderItems(vararg items: FolderItemModel) =
        folderDao.updateFolderItems(*items)

    suspend fun getFirstPage() = pageDao.getFirstPage()

    suspend fun addFolderWithoutPage(folder: FolderModel) = folderDao.addFolderWithoutPage(folder)

    suspend fun updateFolderItemList(itemList: List<FolderItemModel>) =
        folderDao.updateFolderItemList(itemList)
}