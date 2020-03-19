package com.honzikv.androidlauncher.data

import android.app.Activity
import android.content.Intent

class AppListWrapper(activity: Activity) {

    var mutableList: MutableList<AppInfo> = ArrayList()

    init {
        val getAppsIntent = Intent(Intent.ACTION_MAIN, null)
            .addCategory(Intent.CATEGORY_LAUNCHER)

        val resolveInfoList = activity.applicationContext.packageManager
            .queryIntentActivities(getAppsIntent, 0)

        val packageManager = activity.packageManager


        //Map values from resolveInfoList containing app data to {AppInfo} object
        resolveInfoList.forEach {
            mutableList.add(
                AppInfo(
                    it.activityInfo.loadLabel(packageManager).toString(),
                    it.activityInfo.loadIcon(packageManager),
                    it.activityInfo.packageName
                )
            )
        }
    }
}