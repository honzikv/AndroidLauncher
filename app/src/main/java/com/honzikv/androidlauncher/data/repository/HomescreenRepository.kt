package com.honzikv.androidlauncher.data.repository

import androidx.lifecycle.LiveData
import com.honzikv.androidlauncher.data.database.dao.FolderDao
import com.honzikv.androidlauncher.data.database.dao.PageDao
import com.honzikv.androidlauncher.data.model.entity.FolderItemModel
import com.honzikv.androidlauncher.data.model.entity.FolderModel
import com.honzikv.androidlauncher.data.model.entity.PageFolderList
import com.honzikv.androidlauncher.data.model.entity.PageModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext


class HomescreenRepository(
    private val pageDao: PageDao,
    private val folderDao: FolderDao
) {

    private val allPages: LiveData<List<PageModel>> = pageDao.getAllPages()

    private val allFolders: LiveData<List<PageFolderList>> = folderDao.getAllFolders()

    suspend fun addPageAsLast(page: PageModel): Int = withContext(Dispatchers.IO) {
        val pages = pageDao.getAllPagesAsMutable()
        val lastPageNumber = pages[pages.size - 1].pageNumber
        page.pageNumber = lastPageNumber + 1
        return@withContext pageDao.addPage(page)
    }

    suspend fun removePage(page: PageModel) = withContext(Dispatchers.IO) {
        val pages = pageDao.getAllPagesAsMutable()
        for (i in page.pageNumber..pages.size) {
            pages[i].pageNumber = pages[i].pageNumber - 1
        }
        pages.removeAt(page.pageNumber - 1)
        pageDao.updatePageList(pages)
        pageDao.deletePage(page)
    }

    suspend fun addFolderToPage(folderId: Int, pageId: Int) = withContext(Dispatchers.IO) {
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

    fun getPageCount(): Int {
        TODO("return pageCount")
    }

    fun getPage(currentPage: Int): LiveData<PageModel>? {
        TODO("return page")
    }

    fun getFolders(currentPage: Int): LiveData<List<FolderModel>> {
        TODO("return folders")
    }

    fun removeItemFromFolder(item: FolderItemModel, folder: FolderModel) {
        TODO("Not yet implemented")
    }

}