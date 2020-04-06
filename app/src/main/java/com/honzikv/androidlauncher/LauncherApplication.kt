package com.honzikv.androidlauncher

import android.app.Application
import com.honzikv.androidlauncher.data.first.launch.FirstLaunchInitializer
import com.honzikv.androidlauncher.dependency.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

/**
 * This project uses KOIN injector
 */

class LauncherApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Timber.d("Injecting dependencies")
        startKoin {
            androidContext(this@LauncherApplication)
            modules(module)
        }
        Timber.d("Dependencies successfully injected")

        Timber.d("Checking if this is a first start")
        val initializer: FirstLaunchInitializer = get()

        CoroutineScope(Dispatchers.IO).launch {
            if (!initializer.isAppInitialized()) {
                Timber.d("App is not initialized, attempting to create default variables")
                initializer.initialize()
            }
        }
    }

}

