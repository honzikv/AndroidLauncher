package com.honzikv.androidlauncher.data.repository

import androidx.lifecycle.LiveData
import com.honzikv.androidlauncher.data.database.dao.DockDao
import com.honzikv.androidlauncher.data.model.entity.DockItemModel
import com.honzikv.androidlauncher.data.model.entity.DockModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.koin.ext.scope

class DockDataRepository(
    private val dockDao: DockDao
) {

    val dock = getDockInstance()

    val dockItems = dockDao.getItems()

    private fun getDockInstance(): LiveData<DockModel> {
        val dock = dockDao.getDock()
        if (dock.value == null) {
            runBlocking {
                createDefaultDock()
            }
            return dockDao.getDock() as LiveData<DockModel>
        }

        return dock as LiveData<DockModel>
    }

    private suspend fun createDefaultDock() =
        withContext(Dispatchers.IO) { dockDao.createDock(DockModel()) }


}