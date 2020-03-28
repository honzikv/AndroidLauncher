package com.honzikv.androidlauncher

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.honzikv.androidlauncher.data.database.AppInfoDatabase
import com.honzikv.androidlauncher.data.repository.DockDataRepository
import com.honzikv.androidlauncher.data.repository.FolderDataRepository
import com.honzikv.androidlauncher.data.repository.SystemAppsRepository
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.LazyKodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.eagerSingleton
import org.kodein.di.generic.instance
import timber.log.Timber

/**
 * For much cleaner code this project uses KODEIN injection framework to minimize boilerplate code
 * of getting instances of repositories, databases and such.
 */
class LauncherApplication : Application(), KodeinAware {

    override val kodein = Kodein.lazy {

        bind<AppInfoDatabase>() with eagerSingleton {
            Room.databaseBuilder(
                this@LauncherApplication,
                AppInfoDatabase::class.java,
                "app_database"
            )
                .fallbackToDestructiveMigration()
                .build()
        }

        bind<DockDataRepository>() with eagerSingleton {
            DockDataRepository(instance<AppInfoDatabase>().dockDao()) }
        bind<SystemAppsRepository>() with eagerSingleton { SystemAppsRepository() }
        bind<FolderDataRepository>() with eagerSingleton {
            FolderDataRepository(instance<AppInfoDatabase>().folderDao()) }

    }

    companion object {
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        appContext = applicationContext
    }

}