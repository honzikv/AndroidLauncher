package com.honzikv.androidlauncher.data.repository

import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import com.honzikv.androidlauncher.data.database.dao.FolderDao
import com.honzikv.androidlauncher.data.model.entity.FolderItemModel
import com.honzikv.androidlauncher.data.model.entity.FolderModel
import kotlinx.coroutines.runBlocking

class FolderDataRepository(
    private val folderDao: FolderDao
) {

    suspend fun addFolder(folder: FolderModel) = folderDao.addFolder(folder)

    suspend fun getFolder(folderId: Long) = folderDao.getFolder(folderId)

    suspend fun addAppToFolder(folderItem: FolderItemModel, folder: FolderModel) {
        folderItem.folderId = folder.id!!
        folderItem.position = folder.nextAppPosition
        folder.nextAppPosition = folder.nextAppPosition.plus(1)
        folderDao.updateFolder(folder)
        folderDao.updateFolderItem(folderItem)
    }


}