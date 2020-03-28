package com.honzikv.androidlauncher

import android.app.Application
import android.content.Context
import com.honzikv.androidlauncher.dependency.daoModule
import com.honzikv.androidlauncher.dependency.databaseModule
import com.honzikv.androidlauncher.dependency.repositoryModule
import com.honzikv.androidlauncher.dependency.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber

/**
 * For much cleaner code this project uses KOIN injection framework to minimize boilerplate code
 * of getting instances of repositories, databases and such.
 */

class LauncherApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        startKoin {
            androidContext(this@LauncherApplication)
            modules(repositoryModule, databaseModule, daoModule, viewModelModule)
        }
    }

}