package com.honzikv.androidlauncher.data.repository

import androidx.lifecycle.MutableLiveData
import com.honzikv.androidlauncher.data.database.dao.FolderDao
import com.honzikv.androidlauncher.data.model.entity.FolderModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FolderDataRepository constructor(
    private val folderDao: FolderDao
) {

    private var folderList: MutableLiveData<List<FolderModel>>? = null

    suspend fun getFolderList(): MutableLiveData<List<FolderModel>> {
        withContext(Dispatchers.IO) {
            folderList = folderDao.getFolders()
        }
        return folderList as MutableLiveData<List<FolderModel>>
    }

}