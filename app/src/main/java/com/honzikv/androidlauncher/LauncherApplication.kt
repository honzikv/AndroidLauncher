package com.honzikv.androidlauncher

import android.app.Application
import timber.log.Timber

class LauncherApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

    }

}