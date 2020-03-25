package com.honzikv.androidlauncher.data.database

import android.content.BroadcastReceiver
import android.content.Context
import androidx.loader.content.AsyncTaskLoader
import com.honzikv.androidlauncher.data.model.SystemAppModel


class SystemAppLoader(context: Context) : AsyncTaskLoader<MutableList<SystemAppModel>>(context) {

    private val appList = mutableListOf<SystemAppModel>()

    private val receiver: BroadcastReceiver? = null

    private val pckgManager = context.packageManager

    override fun loadInBackground(): MutableList<SystemAppModel> {
        val systemApps = pckgManager.getInstalledApplications(0)
            ?: listOf()

        val appList = mutableListOf<SystemAppModel>()
        systemApps.forEach { appInfo ->
            appList.add(
                SystemAppModel(
                    appInfo.packageName,
                    appInfo.loadIcon(pckgManager),
                    appInfo.loadLabel(pckgManager).toString(),
                    null
                )
            )
        }

        return appList.apply {
            sortedWith(compareBy { it.appName })
        }
    }

    override fun onStartLoading() {
        super.onStartLoading()

        if (appList != null) {
            deliverResult(appList)
        }

    }

    override fun deliverResult(data: MutableList<SystemAppModel>?) {
        super.deliverResult(data)
    }
}

