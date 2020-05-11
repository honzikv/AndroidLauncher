package com.honzikv.androidlauncher.data.repository

import androidx.lifecycle.LiveData
import com.honzikv.androidlauncher.data.database.dao.FolderDao
import com.honzikv.androidlauncher.data.database.dao.PageDao
import com.honzikv.androidlauncher.data.model.FolderItemModel
import com.honzikv.androidlauncher.data.model.FolderModel
import com.honzikv.androidlauncher.data.model.PageModel
import com.honzikv.androidlauncher.data.model.PageWithFolders
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext


class HomescreenRepository(
    private val pageDao: PageDao,
    private val folderDao: FolderDao
) {

    val allPages: LiveData<List<PageWithFolders>> = pageDao.getAllPagesLiveData()

    /**
     * Adds new empty page to the database
     * @return id of added page
     */
    suspend fun addPageAsLast(): Long = withContext(Dispatchers.IO) {
        val lastPage = pageDao.getLastPageNumber()
        return@withContext if (lastPage == null) {
            pageDao.addPage(PageModel(pageNumber = 0))
        }
        else {
            pageDao.addPage(PageModel(pageNumber = lastPage + 1))
        }

    }

    suspend fun addPageAsFirst(): Long = withContext(Dispatchers.IO) {
        val pages = pageDao.getAllPages()
        pages.forEach { it.pageNumber = it.pageNumber + 1 }
        pageDao.updatePageList(pages)
        pageDao.addPage(PageModel())
    }

    suspend fun removePage(page: PageModel) {
        val pages = pageDao.getAllPages()
        for (i in page.pageNumber..pages.size) {
            pages[i].pageNumber = pages[i].pageNumber - 1
        }

        pages.removeAt(page.pageNumber - 1)
        pageDao.updatePageList(pages)
        pageDao.deletePage(page)
    }

    suspend fun addFolderToPage(folderId: Long, pageId: Long) = withContext(Dispatchers.IO) {
        val pageDeferred = async { pageDao.getPage(pageId) }
        val folderDeferred = async { folderDao.getFolder(folderId) }
        val page = pageDeferred.await()
        val folder = folderDeferred.await()

        folder.position = page.nextFolderPosition
        folder.pageId = page.id
        page.nextFolderPosition = page.nextFolderPosition + 1

        pageDao.updatePage(page)
        folderDao.updateFolder(folder)
    }

    suspend fun removeItem(item: FolderItemModel) {
        TODO("Not yet implemented")
    }

    suspend fun getFolder(folderId: Long) = folderDao.getFolder(folderId)

    suspend fun updateFolder(folderModel: FolderModel) = folderDao.updateFolder(folderModel)

    suspend fun deleteFolder(folderModel: FolderModel) = folderDao.deleteFolder(folderModel)

    suspend fun deletePage(pageModel: PageModel) = pageDao.deletePage(pageModel)

    suspend fun addFolder(folderModel: FolderModel): Long = folderDao.addFolder(folderModel)

    fun getPageWithFolders(pageId: Long): LiveData<PageWithFolders> =
        pageDao.getPageWithFolders(pageId)

    suspend fun deletePageWithId(pageId: Long) = pageDao.deletePageWithId(pageId)

    suspend fun deleteFolderWithId(folderId: Long) = folderDao.deleteFolderWithId(folderId)

    suspend fun updateFolders(vararg folders: FolderModel) = folderDao.updateFolders(*folders)

    suspend fun getFolderWithItems(folderId: Long) = folderDao.getFolderWithItems(folderId)

    suspend fun addItemsWithFolderId(items: List<FolderItemModel>) = folderDao.insertItemsWithFolderId(items)

    fun getFolderWithItemsLiveData(folderId: Long) = folderDao.getFolderWithItemsLiveData(folderId)

    suspend fun deleteFolderItem(id: Long) = folderDao.deleteFolderItem(id)

    fun getFolderLiveData(folderId: Long): LiveData<FolderModel> = folderDao.getFolderLiveData(folderId)
}