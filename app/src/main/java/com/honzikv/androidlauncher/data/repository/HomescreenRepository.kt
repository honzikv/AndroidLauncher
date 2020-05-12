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
        Timber.d("pages size = ${pages.size}")
        pages.forEach { it.pageNumber = it.pageNumber + 1 }
        pageDao.updatePageList(pages)
        //PageModel has default value of position as 0
        return@withContext pageDao.addPage(PageModel())
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

        Timber.d("folderPosition = ${page.nextFolderPosition}")
        Timber.d("page = $page")
        folder.position = page.nextFolderPosition
        folder.pageId = page.id
        page.nextFolderPosition = page.nextFolderPosition + 1

        Timber.d("updating page and folder")
        pageDao.updatePage(page)
        folderDao.updateFolder(folder)
    }

    suspend fun deletePage(pageModel: PageModel) = pageDao.deletePage(pageModel)

    suspend fun updateFolder(folderModel: FolderModel) = folderDao.updateFolder(folderModel)

    suspend fun deleteFolder(folderModel: FolderModel) = folderDao.deleteFolder(folderModel)

    suspend fun addFolder(folderModel: FolderModel): Long = folderDao.addFolderWithoutPage(folderModel)

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

    suspend fun updateFolderItems(vararg items: FolderItemModel) = folderDao.updateFolderItems(*items)

    suspend fun getFirstPage() = pageDao.getFirstPage()

    suspend fun addFolderWithoutPage(folder: FolderModel) = folderDao.addFolderWithoutPage(folder)
}