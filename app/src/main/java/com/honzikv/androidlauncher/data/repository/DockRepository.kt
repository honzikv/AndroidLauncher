package com.honzikv.androidlauncher.data.repository

import com.honzikv.androidlauncher.data.database.dao.DockDao
import com.honzikv.androidlauncher.data.model.DockItemModel

class DockRepository(
    private val dockDao: DockDao
) {
    val dockItemsLiveData = dockDao.getAllItemsLiveData()

    suspend fun getAllItems(): List<DockItemModel> = dockDao.getAllItems()

    suspend fun addItems(list: List<DockItemModel>) = dockDao.addItems(list)

    suspend fun addItem(item: DockItemModel) = dockDao.addItem(item)

    suspend fun removeDockItem(id: Long) {
        val allItems = dockDao.getAllItems()

        if ((allItems[allItems.size - 1].id == id)) {
            dockDao.removeItem(allItems[allItems.size - 1])
        } else {
            val item = allItems.find { it.id == id } ?: return
            val itemPosition = allItems.indexOf(item)

            for (i in itemPosition + 1 until allItems.size) {
                allItems[i].position = allItems[i].position - 1
            }
            allItems.removeAt(itemPosition)
            dockDao.removeItem(item)
            dockDao.updateItems(allItems)
        }
    }

}