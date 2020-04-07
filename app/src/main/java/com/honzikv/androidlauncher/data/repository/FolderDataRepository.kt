package com.honzikv.androidlauncher.data.repository

import com.honzikv.androidlauncher.data.database.dao.FolderDao
import com.honzikv.androidlauncher.data.model.dto.FolderItemDto
import com.honzikv.androidlauncher.data.model.dto.FolderDto

class FolderDataRepository(
    private val folderDao: FolderDao
) {

    suspend fun addFolder(folder: FolderDto) = folderDao.addFolder(folder)

    suspend fun getFolder(folderId: Long) = folderDao.getFolder(folderId)

    suspend fun addAppToFolder(folderItem: FolderItemDto, folder: FolderDto) {
        folderItem.folderId = folder.id!!
        folderItem.position = folder.nextAppPosition
        folder.nextAppPosition = folder.nextAppPosition.plus(1)
        folderDao.updateFolder(folder)
        folderDao.updateFolderItem(folderItem)
    }


}