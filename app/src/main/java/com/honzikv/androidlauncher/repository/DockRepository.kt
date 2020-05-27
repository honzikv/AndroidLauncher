package com.honzikv.androidlauncher.repository

import com.honzikv.androidlauncher.database.dao.DockDao
import com.honzikv.androidlauncher.model.DockItemModel

/**
 * Repository pro dok
 */
class DockRepository(
    private val dockDao: DockDao
) {
    /**
     * LiveData se seznamem aplikaci v doku
     */
    val dockItemsLiveData = dockDao.getAllItemsLiveData()

    /**
     * Query vsech [DockItemModel] z tabulky
     */
    suspend fun getAllItems(): List<DockItemModel> = dockDao.getAllItems()

    /**
     * Prida [list] do tabulky
     */
    suspend fun addItems(list: List<DockItemModel>) = dockDao.addItems(list)

    /**
     * Odstrani [DockItemModel] instanci s [id] z tabulky
     */
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
            dockDao.updateItemList(allItems)
        }
    }

    /**
     * Aktualizuje prvky z [itemList] v databazi
     */
    suspend fun updateItemList(itemList: List<DockItemModel>) = dockDao.updateItemList(itemList)


}