package com.honzikv.androidlauncher.data.receiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.honzikv.androidlauncher.data.repository.AppDrawerRepository
import org.koin.core.KoinComponent
import org.koin.core.get

/**
 * This is basically a subscriber object that triggers system app list update every time app package
 * is changed.
 */
class PackageReceiver : BroadcastReceiver(), KoinComponent {

    private val appDrawerRepository: AppDrawerRepository = get()

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context?, intent: Intent?) =
        appDrawerRepository.updateSystemAppList(get())

}