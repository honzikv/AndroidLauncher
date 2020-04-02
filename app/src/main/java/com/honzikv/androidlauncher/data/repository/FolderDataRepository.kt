package com.honzikv.androidlauncher.data.repository

import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import com.honzikv.androidlauncher.data.database.dao.FolderDao
import com.honzikv.androidlauncher.data.model.entity.FolderItemModel
import com.honzikv.androidlauncher.data.model.entity.FolderModel
import kotlinx.coroutines.runBlocking

class FolderDataRepository constructor(
    private val folderDao: FolderDao
) {

    fun addFolder(folder: FolderModel): Int = folderDao.addFolder(folder)

    suspend fun addAppToFolder(folderItem: FolderItemModel, folder: FolderModel) {
        folderItem.folderId = folder.id!!
        folderItem.position = folder.nextAppPosition
        folder.nextAppPosition = folder.nextAppPosition.plus(1)
        folderDao.updateFolder(folder)
        folderDao.updateFolderItem(folderItem)

    }


}