package com.honzikv.androidlauncher.data.repository

import androidx.lifecycle.LiveData
import com.honzikv.androidlauncher.data.database.dao.DockDao
import com.honzikv.androidlauncher.data.model.entity.DockItemModel
import com.honzikv.androidlauncher.data.model.entity.DockModel

class DockDataRepository(
    private val dockDao: DockDao
) {

    fun getDock(): LiveData<DockModel> {
        val dock = dockDao.getDock()
        if (dock.value == null) {
            createDefaultDock()
            return dockDao.getDock() as LiveData<DockModel>
        }

        return dock as LiveData<DockModel>
    }

    private fun createDefaultDock() {
        dockDao.createDock(DockModel())
    }

    suspend fun getDockItems(): List<DockItemModel> = dockDao.getItems()

    suspend fun createDock(dock: DockModel) {
        dockDao.createDock(dock)
    }
}