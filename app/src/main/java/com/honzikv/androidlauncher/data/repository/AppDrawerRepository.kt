package com.honzikv.androidlauncher.data.repository

import android.content.Intent
import android.content.pm.PackageManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.honzikv.androidlauncher.data.model.DrawerApp
import kotlinx.coroutines.*
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

/**
 * [packageManager] - package manager to obtain all app packages
 */
class AppDrawerRepository(private val packageManager: PackageManager) {

    private val appList: MutableLiveData<List<DrawerApp>> = MutableLiveData(mutableListOf())

    fun getAppList(): LiveData<List<DrawerApp>> = appList

    /**
     * Updates systemApps LiveData with new list of system apps
     */
    suspend fun reloadAppList() = withContext(Dispatchers.Default) {
        val apps = mutableListOf<DrawerApp>()

        //Get all launchable apps
        val systemPackages = packageManager.queryIntentActivities(
            Intent(Intent.ACTION_MAIN, null).apply { addCategory(Intent.CATEGORY_LAUNCHER) }, 0
        )

        //Maps each item obtained via package manager to LauncherApp
        systemPackages.forEach { resolveInfo ->
            apps.add(
                DrawerApp(
                    resolveInfo.activityInfo.packageName,
                    resolveInfo.loadLabel(packageManager).toString(),
                    resolveInfo.loadIcon(packageManager)
                )
            )
        }

        //sets new list as LiveData value and sorts it alphabetically
        appList.postValue(apps.apply { sortedWith(compareBy(DrawerApp::label)) })
    }

}
