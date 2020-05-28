package com.honzikv.androidlauncher

import android.app.Application
import com.honzikv.androidlauncher.utils.Initializer
import com.honzikv.androidlauncher.utils.module
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

/**
 * Trida aplikace pro zajisteni nastaveni pocatecnich hodnot pri startu.
 * Zajistuje dependency injection a testovani zda-li se jedna o prvni start
 */
class LauncherApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        //Nastaveni loggeru
        Timber.plant(Timber.DebugTree())
        Timber.d("Injecting dependencies")
        //Inicializace Koin
        startKoin {
            //Nastaveni aplikacniho kontextu
            androidContext(this@LauncherApplication)
            //Nastaveni jednotlivych zavislosti
            modules(module)
        }
        Timber.d("Dependencies successfully injected")

        Timber.d("Checking if this is a first start")
        val initializer: Initializer = get()

        //Inicializace ve vedlejsim vlakne
        CoroutineScope(Dispatchers.Main).launch {
            if (!initializer.isAppInitialized()) {
                Timber.d("App is not initialized, attempting to create default variables")
                initializer.initialize()
            }
        }
    }

}

