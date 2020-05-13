package com.honzikv.androidlauncher

import android.app.Application
import com.honzikv.androidlauncher.util.initializer.Initializer
import com.honzikv.androidlauncher.util.module
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

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
        val initializer: Initializer = get()

        CoroutineScope(Dispatchers.Main).launch {
            if (!initializer.isAppInitialized()) {
                Timber.d("App is not initialized, attempting to create default variables")
                initializer.initialize()
            }
        }
    }

}

