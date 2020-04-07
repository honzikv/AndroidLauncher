package com.honzikv.androidlauncher.data.repository

import androidx.lifecycle.LiveData
import com.honzikv.androidlauncher.data.database.dao.DockDao
import com.honzikv.androidlauncher.data.model.entity.DockDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class DockRepository(
    private val dockDao: DockDao
) {

    val dock = getDockInstance()

    val dockItems = dockDao.getItems()

    private fun getDockInstance(): LiveData<DockDto> {
        val dock = dockDao.getDock()
        if (dock.value == null) {
            runBlocking {
                createDefaultDock()
            }
            return dockDao.getDock() as LiveData<DockDto>
        }

        return dock as LiveData<DockDto>
    }

    private suspend fun createDefaultDock() =
        withContext(Dispatchers.IO) { dockDao.createDock(DockDto()) }

    fun addItem(packageName: String) {

    }


}