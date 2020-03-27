package com.honzikv.androidlauncher.data.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.honzikv.androidlauncher.LauncherApplication
import com.honzikv.androidlauncher.data.database.dao.DockDao
import com.honzikv.androidlauncher.data.database.dao.FolderDao
import com.honzikv.androidlauncher.data.database.dao.UserAppDao
import com.honzikv.androidlauncher.data.model.AppType
import com.honzikv.androidlauncher.data.model.LauncherApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 *
 */
class AppDataRepository @Inject constructor(
    private val userAppDao: UserAppDao,

    private val folderDao: FolderDao,

    private val dockDao: DockDao
) {

    /**
     * All system apps - displayed in drawer
     */
    private val systemApps = MutableLiveData<List<LauncherApp>>()

    private val folders = MutableLiveData<List<LauncherApp>>()

    suspend fun getSystemApps(): MutableLiveData<List<LauncherApp>> {
        withContext(Dispatchers.IO) {
            if (systemApps.value == null) {
                updateSystemApps(LauncherApplication.appContext)
            }
        }
        return systemApps
    }

    suspend fun getUserApps(): MutableLiveData<List<LauncherApp>> {
        withContext(Dispatchers.IO) {
            if (userApps.value == null) {
                loadUserApps(LauncherApplication.appContext)
            }
        }

        return userApps
    }

    private fun loadFolders() {

    }

    /**
     * Updates systemApps LiveData with new list of system apps
     */
    fun updateSystemApps(context: Context) {
        val packageManager = context.packageManager
        val systemPackages = packageManager.getInstalledApplications(0) ?: listOf()
        val systemAppsList = mutableListOf<LauncherApp>()

        //Maps each item obtained via package manager to LauncherApp for further use
        systemPackages.forEach { systemAppInfo ->
            systemAppsList.add(
                LauncherApp(
                    systemAppInfo.packageName,
                    systemAppInfo.loadIcon(packageManager),
                    systemAppInfo.loadLabel(packageManager).toString()
                )
            )
        }

        //sets new list as LiveData value and sorts it alphabetically
        systemApps.value = systemAppsList.apply {
            sortedWith(compareBy(LauncherApp::label))
        }
    }

}