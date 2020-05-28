package com.honzikv.androidlauncher.viewmodel

import android.content.pm.PackageManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.honzikv.androidlauncher.model.DockItemModel
import com.honzikv.androidlauncher.model.DrawerApp
import com.honzikv.androidlauncher.repository.DockRepository
import com.honzikv.androidlauncher.utils.BackgroundTransformations
import com.honzikv.androidlauncher.utils.Event
import com.honzikv.androidlauncher.utils.MAX_ITEMS_IN_DOCK
import kotlinx.coroutines.launch

/**
 * View model pro dok
 */
class DockViewModel(
    private val dockRepository: DockRepository,
    private val packageManager: PackageManager
) : ViewModel() {

    companion object {
        const val DOCK_FULL_NOTHING_ADDED = "Dock is full and no item was added"
        const val DOCK_IS_FULL_SOMETHING_ADDED = "Dock is full, not all items were added"
    }

    /**
     * Pro callback s chybou pro ukladani aplikaci do doku
     */
    private val dockPostErrorMutable = MutableLiveData<Event<String>>()
    fun getDockPostError() = dockPostErrorMutable as LiveData<Event<String>>

    /**
     * Aplikace v doku spolu s nactenymi ikonami a popisky
     */
    val dockItems = BackgroundTransformations.map(dockRepository.dockItemsLiveData) { items ->
        return@map items.apply {
            forEach { item ->
                val info = packageManager.getApplicationInfo(item.packageName, 0)
                item.label = info.loadLabel(packageManager).toString()
                item.icon = info.loadIcon(packageManager)
            }
        }
    }

    /**
     * Vrati vsechny aplikace v doku
     */
    private suspend fun getAllItems() = dockRepository.getAllItems()

    /**
     * Prida aplikace do doku
     */
    fun addItemsToDock(selectedApps: MutableList<DrawerApp>) = viewModelScope.launch {
        val items = getAllItems()
        var newItems = mutableListOf<DockItemModel>()

        var insertPosition = if (items.isEmpty()) {
            -1
        } else {
            items.maxBy { it.position }!!.position
        }

        //Filtrovani duplicitnich aplikaci
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

        //Error handling pokud prekrocime limit v doku
        if (items.size + newItems.size > MAX_ITEMS_IN_DOCK) {
            val addCount = MAX_ITEMS_IN_DOCK - items.size

            if (addCount == 0) {
                //nic se nepridalo
                dockPostErrorMutable.postValue(
                    Event(
                        DOCK_FULL_NOTHING_ADDED
                    )
                )
                return@launch
            }

            newItems = newItems.subList(0, addCount)
            //pridalo se neco, ale nejake aplikace uz se nevesly
            dockPostErrorMutable.postValue(
                Event(
                    DOCK_IS_FULL_SOMETHING_ADDED
                )
            )
        }

        dockRepository.addItems(newItems)
    }

    /**
     * Odstrani aplikaci s [id] z doku
     */
    fun removeItem(id: Long) = viewModelScope.launch {
        dockRepository.removeDockItem(id)
    }

    /**
     * Aktualizuje seznam aplikaci [itemList] v doku
     */
    fun updateItemList(itemList: List<DockItemModel>) = viewModelScope.launch {
        dockRepository.updateItemList(itemList)
    }
}
