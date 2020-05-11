package com.honzikv.androidlauncher.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.honzikv.androidlauncher.MAX_ITEMS_IN_DOCK
import com.honzikv.androidlauncher.data.model.DockItemModel
import com.honzikv.androidlauncher.data.model.DrawerApp
import com.honzikv.androidlauncher.data.repository.DockRepository
import com.honzikv.androidlauncher.exception.DockIsFullException
import kotlinx.coroutines.launch

class DockViewModel(
    private val dockRepository: DockRepository
) : ViewModel() {

    companion object {
        const val DOCK_FULL_NOTHING_ADDED = "Dock is full and no item was added"
        const val DOCK_IS_FULL_SOMETHING_ADDED = "Dock is full, not all items were added"
    }

    val dockItems = dockRepository.dockItemsLiveData

    fun addItems(list: List<DockItemModel>) = viewModelScope.launch {
        dockRepository.addItems(list)
    }

    private suspend fun getAllItems() = dockRepository.getAllItems()

    fun addItemsToDock(selectedApps: MutableList<DrawerApp>) = viewModelScope.launch {
        val items = getAllItems()
        val newItems = mutableListOf<DockItemModel>()

        var insertPosition = if (items.isEmpty()) {
            -1 //-1 so it is inserted to 0th index
        } else {
            items.maxBy { it.position }!!.position
        }

        //Filter duplicates
        selectedApps.forEach { app ->
            if (!items.any { it.packageName == app.packageName }) {
                insertPosition += 1
                newItems.add(
                    DockItemModel(
                        packageName = app.packageName,
                        position = insertPosition
                    )
                )
            }
        }

        //if there are too many items selected add only those that dont exceed the MAX_ITEMS_IN_DOCK limit
        if (items.size + newItems.size > MAX_ITEMS_IN_DOCK) {
            val addCount = MAX_ITEMS_IN_DOCK - items.size
            if (addCount == 0) {
                throw DockIsFullException(DOCK_FULL_NOTHING_ADDED)
            }

            for (i in 0 until addCount) {
                dockRepository.addItem(newItems[i])
            }
            throw  DockIsFullException(DOCK_IS_FULL_SOMETHING_ADDED)
        }

        //Else add all items
        dockRepository.addItems(newItems)
    }

    fun removeItem(id: Long) = viewModelScope.launch {
        dockRepository.removeDockItem(id)
    }
}