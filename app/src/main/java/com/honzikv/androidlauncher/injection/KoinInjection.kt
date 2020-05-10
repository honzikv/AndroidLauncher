package com.honzikv.androidlauncher.injection

import android.content.Context
import androidx.room.Room
import com.honzikv.androidlauncher.data.database.LauncherDatabase
import com.honzikv.androidlauncher.data.first.launch.FirstLaunchInitializer
import com.honzikv.androidlauncher.data.repository.*
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
    single { AppDrawerRepository(androidContext().packageManager) }
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
        FirstLaunchInitializer(
            get(), get(), androidContext().packageManager, get(), get()
        )
    }

    //Viewmodels
    viewModel { HomescreenViewModel(get(), androidContext().packageManager) }
    viewModel { AppDrawerViewModel(get(), get(), get()) }
    viewModel { DockViewModel(get()) }
    viewModel { SettingsViewModel(get(), get()) }
}