package com.honzikv.androidlauncher.data.repository

import android.content.Context
import android.content.Intent
import com.honzikv.androidlauncher.data.AppInfo

/**
 * Repository for system info - applist, app intents and such.
 */
class SystemDataRepository private constructor() {

    companion object {

        @Volatile
        private var instance: SystemDataRepository? = null

        fun getInstance(): SystemDataRepository {
            return instance
                ?: synchronized(this) {
                instance
                    ?: SystemDataRepository().also { instance = it }
            }
        }
    }

    /**
     * Returns app list corresponding to context
     */
    fun getAppList(context: Context) : List<AppInfo> {

        val queryAppsIntent = Intent(Intent.ACTION_MAIN, null)
            .addCategory(Intent.CATEGORY_LAUNCHER)

        val packageManager = context.packageManager

        val appListAsResolveInfoList =
            packageManager.queryIntentActivities(queryAppsIntent, 0)

        val appList = mutableListOf<AppInfo>()
        appListAsResolveInfoList.forEach {
            appList.add(
                AppInfo(
                    it.activityInfo.loadLabel(packageManager).toString(),
                    it.activityInfo.loadIcon(packageManager),
                    it.activityInfo.packageName
                )
            )
        }

        return appList
    }


}