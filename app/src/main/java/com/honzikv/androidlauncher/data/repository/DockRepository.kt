package com.honzikv.androidlauncher.data.repository

import androidx.lifecycle.LiveData
import com.honzikv.androidlauncher.data.database.dao.DockDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class DockRepository(
    private val dockDao: DockDao
) {

    val dockItems = dockDao.getItems()


    fun addItem(packageName: String) {

    }


}