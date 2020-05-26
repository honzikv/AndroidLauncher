package com.honzikv.androidlauncher.viewmodel

import android.content.pm.PackageManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.honzikv.androidlauncher.utils.MAX_ITEMS_IN_DOCK
import com.honzikv.androidlauncher.model.DockItemModel
import com.honzikv.androidlauncher.model.DrawerApp
import com.honzikv.androidlauncher.repository.DockRepository
import com.honzikv.androidlauncher.utils.BackgroundTransformations
import com.honzikv.androidlauncher.utils.callback.Event
import kotlinx.coroutines.launch
import timber.log.Timber

class DockViewModel(
    private val dockRepository: DockRepository,
    private val packageManager: PackageManager
) : ViewModel() {

    companion object {
        const val DOCK_FULL_NOTHING_ADDED = "Dock is full and no item was added"
        const val DOCK_IS_FULL_SOMETHING_ADDED = "Dock is full, not all items were added"
    }

    init {
        Timber.d("creating dock view model")
    }

    /**
     * Observable to notify error when adding items to dock
     */
    private val dockPostErrorMutable = MutableLiveData<Event<String>>()
    fun getDockPostError() = dockPostErrorMutable as LiveData<Event<String>>

    val dockItems = BackgroundTransformations.map(dockRepository.dockItemsLiveData) { items ->
        return@map items.apply {
            forEach { item ->
                val info = packageManager.getApplicationInfo(item.packageName, 0)
                item.label = info.loadLabel(packageManager).toString()
                item.icon = info.loadIcon(packageManager)
            }
        }
    }

    private suspend fun getAllItems() = dockRepository.getAllItems()

    fun addItemsToDock(selectedApps: MutableList<DrawerApp>) = viewModelScope.launch {
        val items = getAllItems()
        var newItems = mutableListOf<DockItemModel>()

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
            Timber.d("dock limit reached")
            val addCount = MAX_ITEMS_IN_DOCK - items.size
            if (addCount == 0) {
                Timber.d("dock full error")
                dockPostErrorMutable.postValue(Event(DOCK_FULL_NOTHING_ADDED))
                return@launch
            }

            Timber.d("cant add all items to dock")
            newItems = newItems.subList(0, addCount)
            dockPostErrorMutable.postValue(Event(DOCK_IS_FULL_SOMETHING_ADDED))
        }

        dockRepository.addItems(newItems)
    }

    fun removeItem(id: Long) = viewModelScope.launch {
        dockRepository.removeDockItem(id)
    }

    fun updateItemList(itemList: List<DockItemModel>) = viewModelScope.launch {
        dockRepository.updateItemList(itemList)
    }
}
