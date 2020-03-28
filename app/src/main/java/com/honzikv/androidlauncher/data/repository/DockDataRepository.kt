package com.honzikv.androidlauncher.data.repository

import com.honzikv.androidlauncher.data.database.dao.DockDao
import com.honzikv.androidlauncher.data.model.entity.DockModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DockDataRepository constructor(
    private val dockDao: DockDao
) {

    private var dock: DockModel? = null

    suspend fun getDock(): DockModel {
        withContext(Dispatchers.IO) {
            dock = dockDao.getInstance()
        }

        return dock as DockModel
    }
}