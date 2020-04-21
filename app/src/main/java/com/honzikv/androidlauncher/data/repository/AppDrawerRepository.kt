package com.honzikv.androidlauncher.data.repository

import android.content.pm.PackageManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.honzikv.androidlauncher.data.model.DrawerApp

/**
 * [packageManager] - package manager to obtain all app packages
 */
class AppDrawerRepository(private val packageManager: PackageManager) {

    /**
     * All system apps - displayed in drawer
     */
    private var systemAppList = MutableLiveData<List<DrawerApp>>()

    /**
     * Lazy get app data from system
     */
    fun getSystemApps(): LiveData<List<DrawerApp>> {
        if (systemAppList.value == null) {
            updateSystemAppList()
        }
        return systemAppList
    }

    /**
     * Updates systemApps LiveData with new list of system apps
     */
    fun updateSystemAppList() {
        val systemPackages = packageManager.getInstalledApplications(0) ?: listOf()
        val apps = mutableListOf<DrawerApp>()

        //Maps each item obtained via package manager to LauncherApp
        systemPackages.forEach { systemAppInfo ->
            apps.add(
                DrawerApp(
                    systemAppInfo.packageName,
                    systemAppInfo.loadLabel(packageManager).toString(),
                    systemAppInfo.loadIcon(packageManager)
                )
            )
        }

        //sets new list as LiveData value and sorts it alphabetically
        this.systemAppList.postValue(apps.apply { sortedWith(compareBy(DrawerApp::label)) })
    }


}