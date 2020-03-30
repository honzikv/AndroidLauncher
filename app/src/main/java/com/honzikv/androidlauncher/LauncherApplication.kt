package com.honzikv.androidlauncher

import android.app.Application
import android.content.Context
import androidx.lifecycle.lifecycleScope
import com.honzikv.androidlauncher.data.first.launch.FirstLaunchInitializer
import com.honzikv.androidlauncher.dependency.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.KoinContextHandler.get
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.ext.scope
import timber.log.Timber
import kotlinx.coroutines.launch

/**
 * For much cleaner code this project uses KOIN injection framework to minimize boilerplate code
 * of getting instances of repositories, databases and such.
 */

class LauncherApplication : Application() {

    private val initializer: FirstLaunchInitializer by inject<FirstLaunchInitializer>()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        startKoin {
            androidContext(this@LauncherApplication)
            modules(repositoryModule, databaseModule, daoModule, viewModelModule, utilsModule)
        }

    }

}

