package com.honzikv.androidlauncher.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.honzikv.androidlauncher.data.database.dao.DockDao
import com.honzikv.androidlauncher.data.model.entity.DockItemModel
import com.honzikv.androidlauncher.data.model.entity.DockModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DockDataRepository constructor(
    private val dockDao: DockDao
) {

    suspend fun getDock() : LiveData<DockModel> = dockDao.getInstance()

    suspend fun getDockItems() : List<DockItemModel> = dockDao.getItems()
}