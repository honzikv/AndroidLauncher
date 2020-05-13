package com.honzikv.androidlauncher.util

import android.content.Context
import androidx.room.Room
import com.honzikv.androidlauncher.database.LauncherDatabase
import com.honzikv.androidlauncher.repository.*
import com.honzikv.androidlauncher.util.initializer.Initializer
import com.honzikv.androidlauncher.viewmodel.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val module = module {

    single {
        Room.databaseBuilder(get(), LauncherDatabase::class.java, "app_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    //Daos
    single { get<LauncherDatabase>().dockDao() }
    single { get<LauncherDatabase>().folderDao() }
    single { get<LauncherDatabase>().homescreenPageDao() }
    single { get<LauncherDatabase>().themeProfileDao() }

    //Repos
    single { HomescreenRepository(get(), get()) }
    single { DockRepository(get()) }
    single { FolderDataRepository(get()) }
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

    //sharedprefs
    single { androidContext().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE) }

    single {
        Initializer(
            get(), get(), get(), androidContext().packageManager
        )
    }

    //Viewmodels
    viewModel { HomescreenViewModel(get(), androidContext().packageManager) }
    viewModel { DrawerViewModel(get(), get(), get()) }
    viewModel { DockViewModel(get(), androidContext().packageManager) }
    viewModel { SettingsViewModel(get(), get()) }
}