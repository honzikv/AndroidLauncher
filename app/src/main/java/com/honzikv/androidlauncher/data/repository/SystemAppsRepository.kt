package com.honzikv.androidlauncher.data.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.honzikv.androidlauncher.LauncherApplication
import com.honzikv.androidlauncher.data.model.DrawerAppModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 *
 */
class SystemAppsRepository constructor(
) {

    /**
     * All system apps - displayed in drawer
     */
    private var systemAppList = MutableLiveData<List<DrawerAppModel>>()

    suspend fun getSystemApps(): MutableLiveData<List<DrawerAppModel>> {
        withContext(Dispatchers.IO) {
            if (systemAppList.value == null) {
                systemAppList = MutableLiveData()
                updateSystemAppList(LauncherApplication.appContext)
            }
        }
        return systemAppList as MutableLiveData<List<DrawerAppModel>>
    }


    /**
     * Updates systemApps LiveData with new list of system apps
     */
    fun updateSystemAppList(context: Context) {
        val packageManager = context.packageManager
        val systemPackages = packageManager.getInstalledApplications(0) ?: listOf()
        val systemAppsList = mutableListOf<DrawerAppModel>()

        //Maps each item obtained via package manager to LauncherApp for further use
        systemPackages.forEach { systemAppInfo ->
            systemAppsList.add(
                DrawerAppModel(
                    systemAppInfo.packageName,
                    systemAppInfo.loadLabel(packageManager).toString()
                )
            )
        }

        //sets new list as LiveData value and sorts it alphabetically
        systemAppList.value = systemAppsList.apply {
            sortedWith(compareBy(DrawerAppModel::label))
        }
    }


}