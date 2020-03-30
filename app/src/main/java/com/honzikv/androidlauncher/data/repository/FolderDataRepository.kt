package com.honzikv.androidlauncher.data.repository

import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import com.honzikv.androidlauncher.data.database.dao.FolderDao
import com.honzikv.androidlauncher.data.model.entity.FolderItemModel
import com.honzikv.androidlauncher.data.model.entity.FolderModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FolderDataRepository constructor(
    private val folderDao: FolderDao
) {

    fun getFolderList(page: Int): MutableLiveData<List<FolderModel>> {
        return folderDao.getFoldersOnPageLiveData(page)
    }

    fun addFolder(folder: FolderModel): Int = folderDao.addFolder(folder)

    fun addAppToFolder(folderItem: FolderItemModel, folder: FolderModel) {
        folderItem.folderId = folder.id!!
        folderItem.position = folder.nextAppPosition
        folder.nextAppPosition = folder.nextAppPosition.plus(1)
        folderDao.updateFolder(folder)
        folderDao.updateFolderItem(folderItem)
    }

    fun getFolder(folderId: Int): FolderModel {
        return folderDao.getFolder(folderId) ?: throw Resources.NotFoundException()
    }

    fun getFolderItems(folderId: Int) = folderDao.getFolderItems(folderId)

}