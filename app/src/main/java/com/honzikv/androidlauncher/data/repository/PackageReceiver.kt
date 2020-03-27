package com.honzikv.androidlauncher.data.repository

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.honzikv.androidlauncher.LauncherApplication
import javax.inject.Inject

/**
 * This is basically a subscriber object that notifies
 */
class PackageReceiver(@Inject private val repository: AppDataRepository) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) =
        repository.updateSystemApps(LauncherApplication.appContext)

}