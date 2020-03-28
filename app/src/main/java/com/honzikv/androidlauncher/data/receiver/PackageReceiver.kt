package com.honzikv.androidlauncher.data.receiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.honzikv.androidlauncher.LauncherApplication
import com.honzikv.androidlauncher.data.repository.SystemAppsRepository
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.direct
import org.kodein.di.generic.instance

/**
 * This is basically a subscriber object that triggers system app list update every time app package
 * is changed.
 */
class PackageReceiver(override val kodein: Kodein) : BroadcastReceiver(), KodeinAware {

    private val systemAppsRepository by kodein.instance<SystemAppsRepository>()

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context?, intent: Intent?) =
        systemAppsRepository.updateSystemAppList(LauncherApplication.appContext)

}