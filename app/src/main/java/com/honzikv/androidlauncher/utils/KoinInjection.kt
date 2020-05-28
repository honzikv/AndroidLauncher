package com.honzikv.androidlauncher.utils

import android.content.Context
import androidx.room.Room
import com.honzikv.androidlauncher.database.LauncherDatabase
import com.honzikv.androidlauncher.repository.*
import com.honzikv.androidlauncher.viewmodel.DockViewModel
import com.honzikv.androidlauncher.viewmodel.DrawerViewModel
import com.honzikv.androidlauncher.viewmodel.HomescreenViewModel
import com.honzikv.androidlauncher.viewmodel.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Protoze vetsina komponent potrebuje nekolik zavislosti najednou rozhodl jsem se pouzit
 * Dependency Injection Framework - v podstate se jedna o vytvoreni singletonu, ktere muzeme ziskat
 * ze vsech Fragmentu / Aktivit a z trid implementujici KoinComponent.
 */
val module = module {

    single {
        Room.databaseBuilder(get(), LauncherDatabase::class.java, "app_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    //Dao
    single { get<LauncherDatabase>().dockDao() }
    single { get<LauncherDatabase>().folderDao() }
    single { get<LauncherDatabase>().homescreenPageDao() }
    single { get<LauncherDatabase>().themeProfileDao() }

    //Repository
    single { HomescreenRepository(get(), get()) }
    single { DockRepository(get()) }
    single { DrawerRepository(androidContext().packageManager) }
    single {
        AppSettingsRepository(
            androidContext().getSharedPreferences(
                APP_PREFERENCES,
                Context.MODE_PRIVATE
            )
        )
    }
    single {
        AppThemeRepository(
            androidContext().getSharedPreferences(
                APP_PREFERENCES,
                Context.MODE_PRIVATE
            ), get()
        )
    }

    //Shared Preferences
    single { androidContext().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE) }

    //First Launch Initializer
    single {
        Initializer(
            get(),
            get(),
            get(),
            androidContext().packageManager
        )
    }

    //Viewmodel
    viewModel { HomescreenViewModel(get(), androidContext().packageManager) }
    viewModel { DrawerViewModel(get()) }
    viewModel { DockViewModel(get(), androidContext().packageManager) }
    viewModel { SettingsViewModel(get(), get()) }
}