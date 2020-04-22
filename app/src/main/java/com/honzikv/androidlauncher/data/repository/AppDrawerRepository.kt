package com.honzikv.androidlauncher.data.repository

import android.content.pm.PackageManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.honzikv.androidlauncher.data.model.DrawerApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * [packageManager] - package manager to obtain all app packages
 */
class AppDrawerRepository(private val packageManager: PackageManager) {

    private val appList: MutableLiveData<List<DrawerApp>> = MutableLiveData(mutableListOf())

    fun getAppList(): LiveData<List<DrawerApp>> {
        return appList
    }

    /**
     * Updates systemApps LiveData with new list of system apps
     */
    suspend fun reloadAppList() = withContext(Dispatchers.Default) {
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
        appList.postValue(apps.apply {
            sortedWith(compareBy(DrawerApp::label))
        })
    }


}