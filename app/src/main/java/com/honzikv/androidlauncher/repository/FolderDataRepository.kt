package com.honzikv.androidlauncher.repository

import com.honzikv.androidlauncher.database.dao.FolderDao
import com.honzikv.androidlauncher.model.FolderModel

class FolderDataRepository(
    private val folderDao: FolderDao
) {

    suspend fun addFolder(folder: FolderModel) = folderDao.addFolderWithoutPage(folder)

    suspend fun getFolder(folderId: Long) = folderDao.getFolder(folderId)

}