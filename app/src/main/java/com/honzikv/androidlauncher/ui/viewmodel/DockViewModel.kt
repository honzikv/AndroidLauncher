package com.honzikv.androidlauncher.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.honzikv.androidlauncher.data.model.LauncherAppModel
import com.honzikv.androidlauncher.data.model.entity.DockItemModel
import com.honzikv.androidlauncher.data.model.entity.DockModel
import com.honzikv.androidlauncher.data.repository.DockDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DockViewModel(
    private val dockDataDataRepository: DockDataRepository,
    private val applicationContext: Context
) : ViewModel() {

    private lateinit var dock: LiveData<DockModel>

    private lateinit var dockItems: MutableLiveData<List<LauncherAppModel>>

    suspend fun getDock(): LiveData<DockModel> {
        withContext(Dispatchers.IO) {
            if (!::dock.isInitialized) {
                dock = dockDataDataRepository.getDock()
            }
        }
        return dock
    }

    suspend fun addItem(item: LauncherAppModel) {

    }

    suspend fun getDockItems(): LiveData<List<LauncherAppModel>> {
        withContext(Dispatchers.IO) {
            if (!::dockItems.isInitialized) {
                dockItems = MutableLiveData()
            }

            val dockItemsDB = dockDataDataRepository.getDockItems()
            val mappedList = mutableListOf<LauncherAppModel>()
            val packageManager = applicationContext.packageManager

            //Mapping database objects to frontend objects
            dockItemsDB.forEach { item ->
                val appInfo = packageManager.getApplicationInfo(item.systemAppPackageName, 0)
                mappedList.add(
                    LauncherAppModel(
                        item.systemAppPackageName,
                        packageManager.getApplicationLabel(appInfo).toString(),
                        packageManager.getApplicationIcon(item.systemAppPackageName)
                    )
                )
            }

            dockItems.value = mappedList
        }
        return dockItems
    }
}