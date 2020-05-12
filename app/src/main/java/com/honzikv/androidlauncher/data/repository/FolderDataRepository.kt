package com.honzikv.androidlauncher.data.repository

import com.honzikv.androidlauncher.data.database.dao.FolderDao
import com.honzikv.androidlauncher.data.model.FolderModel

class FolderDataRepository(
    private val folderDao: FolderDao
) {

    suspend fun addFolder(folder: FolderModel) = folderDao.addFolderWithoutPage(folder)

    suspend fun getFolder(folderId: Long) = folderDao.getFolder(folderId)

}