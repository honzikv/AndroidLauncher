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

    val allPages: LiveData<List<PageWithFolders>> = pageDao.getAllPages()

    suspend fun addPageAsLast(page: PageModel): Long = withContext(Dispatchers.IO) {
        val pages = pageDao.getAllPagesAsMutable()
        val lastPageNumber = if (pages.isEmpty()) {
            -1
        } else {
            pages[pages.size - 1].pageNumber
        }

        page.pageNumber = lastPageNumber + 1
        return@withContext pageDao.addPage(page)
    }

    suspend fun removePage(page: PageModel) {
        val pages = pageDao.getAllPagesAsMutable()
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

}