package com.honzikv.androidlauncher.data.repository

import android.content.res.Resources
import com.honzikv.androidlauncher.data.database.dao.FolderDao
import com.honzikv.androidlauncher.data.database.dao.HomescreenPageDao
import com.honzikv.androidlauncher.data.model.entity.HomescreenPageModel


class HomescreenPageRepository(
    private val homescreenPageDao: HomescreenPageDao,
    private val folderDao: FolderDao
) {

    fun addPageAsLast(page: HomescreenPageModel): Int {
        val pages = homescreenPageDao.getAllPagesByPageNumberAsc()
        val lastPageNumber = pages[pages.size - 1].pageNumber
        page.pageNumber = lastPageNumber + 1
        return homescreenPageDao.addPage(page)
    }

    fun removePage(page: HomescreenPageModel) {
        val pages = homescreenPageDao.getAllPagesByPageNumberAsc()
        for (i in page.pageNumber..pages.size) {
            pages[i].pageNumber = pages[i].pageNumber - 1
        }
        pages.removeAt(page.pageNumber - 1)
        homescreenPageDao.updatePageList(pages)
        homescreenPageDao.deletePage(page)
    }

    fun addFolderToPage(folderId: Int, pageId: Int) {
        val page = homescreenPageDao.getPage(pageId)
            ?: throw Resources.NotFoundException()
        val folder = folderDao.getFolder(folderId)
            ?: throw Resources.NotFoundException()

        val folderPosition = page.nextFolderPosition
        folder.position = folderPosition
        folder.pageId = page.id
        page.nextFolderPosition = page.nextFolderPosition + 1

        homescreenPageDao.updatePage(page)
        folderDao.updateFolder(folder)
    }

}