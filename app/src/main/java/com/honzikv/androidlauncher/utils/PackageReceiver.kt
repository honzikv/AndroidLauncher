package com.honzikv.androidlauncher.utils

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.honzikv.androidlauncher.repository.DrawerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * Toto je subscriber, ktery aktualizuje aplikace v [drawerRepository] pri odinstalovani nebo nainstalovani
 * nove aplikace
 */
class PackageReceiver : BroadcastReceiver(), KoinComponent {

    private val drawerRepository: DrawerRepository by inject()

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context?, intent: Intent?) {
        GlobalScope.launch(Dispatchers.Default) {
            drawerRepository.reloadAppList()
        }
    }


}