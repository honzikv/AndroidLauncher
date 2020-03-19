package com.honzikv.androidlauncher.data

import android.app.Activity
import android.content.Intent
import timber.log.Timber

class LauncherData(private val activity: Activity) {

    val appList: MutableList<AppInfo> = ArrayList()

    init {
        Timber.d("Initializing instance")
        populateList()
        Timber.d("Successfully initialized")
    }

    private fun populateList() {
        val getAppsIntent = Intent(Intent.ACTION_MAIN, null)
            .addCategory(Intent.CATEGORY_LAUNCHER)

        val resolveInfoList = activity.applicationContext.packageManager
            .queryIntentActivities(getAppsIntent, 0)

        val packageManager = activity.packageManager

        //Map values from resolveInfoList containing app data to AppInfo object
        resolveInfoList.forEach {
            appList.add(
                AppInfo(
                    it.activityInfo.loadLabel(packageManager).toString(),
                    it.activityInfo.loadIcon(packageManager),
                    it.activityInfo.packageName
                )
            )
        }
    }

    fun refreshList() {
        populateList()
    }
}