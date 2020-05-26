package com.honzikv.androidlauncher.repository

import android.content.Intent
import android.content.pm.PackageManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.honzikv.androidlauncher.model.DrawerApp
import kotlinx.coroutines.*

/**
 * [packageManager] - package manager to obtain all app packages
 */
class DrawerRepository(private val packageManager: PackageManager) {

    /**
     * LiveData objekt se vsemi aplikacemi
     */
    private val appList: MutableLiveData<List<DrawerApp>> = MutableLiveData(mutableListOf())

    /**
     * Funkce pro ziskani LiveData objektu se vsemi aplikacemi
     */
    fun getAppList(): LiveData<List<DrawerApp>> = appList

    /**
     * Aktualizace [appList]. Funkce ziska vsechny nainstalovane aplikace v zarizeni a namapuje je
     * na [DrawerApp] instance
     */
    suspend fun reloadAppList() = withContext(Dispatchers.Default) {
        val apps = mutableListOf<DrawerApp>()

        //Intent pro ziskani vsech spustitelnych aplikaci
        val systemPackages = packageManager.queryIntentActivities(
            Intent(Intent.ACTION_MAIN, null).apply { addCategory(Intent.CATEGORY_LAUNCHER) }, 0
        )

        //Namapovani na [DrawerApp]
        systemPackages.forEach { resolveInfo ->
            apps.add(
                DrawerApp(
                    resolveInfo.activityInfo.packageName,
                    resolveInfo.loadLabel(packageManager).toString(),
                    resolveInfo.loadIcon(packageManager)
                )
            )
        }

        //Posle seznam aplikaci do [appList] live dat a seradi je podle abecedy
        appList.postValue(apps.sortedWith(compareBy(DrawerApp::label)))
    }

}
