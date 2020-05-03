package com.honzikv.androidlauncher.data.repository

import com.honzikv.androidlauncher.data.database.dao.FolderDao
import com.honzikv.androidlauncher.data.model.FolderModel
import com.honzikv.androidlauncher.data.model.FolderItemModel
import timber.log.Timber

class FolderDataRepository(
    private val folderDao: FolderDao
) {

    suspend fun addFolder(folder: FolderModel) = folderDao.addFolder(folder)

    suspend fun getFolder(folderId: Long) = folderDao.getFolder(folderId)

    suspend fun addAppToFolder(folderItem: FolderItemModel, folder: FolderModel) {
        Timber.d("Adding app to folder with id = ${folder.id}")
        folderItem.folderId = folder.id!!
        folderItem.position = folder.nextAppPosition
        Timber.d("Item's position in folder is ${folderItem.position}")
        folder.nextAppPosition = folder.nextAppPosition.plus(1)
        folderDao.updateFolder(folder)
        folderDao.addFolderItem(folderItem)
        Timber.d("Item successfully added to the folder")
    }


}