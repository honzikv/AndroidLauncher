package com.honzikv.androidlauncher

import android.app.Application
import com.honzikv.androidlauncher.data.first.launch.FirstLaunchInitializer
import com.honzikv.androidlauncher.dependency.*
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

/**
 * For much cleaner code this project uses KOIN injection framework to minimize boilerplate code
 * of getting instances of repositories, databases and such.
 */

class LauncherApplication : Application() {

    private val initializer: FirstLaunchInitializer by inject<FirstLaunchInitializer>()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Timber.d("Injecting dependencies")
        startKoin {
            androidContext(this@LauncherApplication)
            modules(module)
        }
        Timber.d("Dependencies successfully injected")

    }

}

