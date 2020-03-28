package com.honzikv.androidlauncher.data.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.honzikv.androidlauncher.data.model.LauncherAppModel

/**
 *
 */
class SystemAppsRepository(private val context: Context) {

//    /**
//     * All system apps - displayed in drawer
//     */
//    private var systemAppList = MutableLiveData<List<LauncherAppModel>>()
//
//    fun getSystemApps(): MutableLiveData<List<LauncherAppModel>> {
//        if (systemAppList.value == null) {
//            systemAppList = MutableLiveData()
//            updateSystemAppList(context)
//        }
//        return systemAppList
//    }
//
//
//    /**
//     * Updates systemApps LiveData with new list of system apps
//     */
//    fun updateSystemAppList(context: Context) {
//        val packageManager = context.packageManager
//        val systemPackages = packageManager.getInstalledApplications(0) ?: listOf()
//        val systemAppsList = mutableListOf<LauncherAppModel>()
//
//        //Maps each item obtained via package manager to LauncherApp for further use
//        systemPackages.forEach { systemAppInfo ->
//            systemAppsList.add(
//                LauncherAppModel(
//                    systemAppInfo.packageName,
//                    systemAppInfo.loadLabel(packageManager).toString()
//                )
//            )
//        }
//
//        //sets new list as LiveData value and sorts it alphabetically
//        systemAppList.value = systemAppsList.apply {
//            sortedWith(compareBy(LauncherAppModel::label))
//        }
//    }


}