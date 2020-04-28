package com.honzikv.androidlauncher.injection

import android.content.Context
import androidx.room.Room
import com.honzikv.androidlauncher.data.database.LauncherDatabase
import com.honzikv.androidlauncher.data.first.launch.FirstLaunchInitializer
import com.honzikv.androidlauncher.data.repository.DockRepository
import com.honzikv.androidlauncher.data.repository.FolderDataRepository
import com.honzikv.androidlauncher.data.repository.HomescreenRepository
import com.honzikv.androidlauncher.data.repository.AppDrawerRepository
import com.honzikv.androidlauncher.data.repository.APP_PREFERENCES
import com.honzikv.androidlauncher.data.repository.AppSettingsRepository
import com.honzikv.androidlauncher.data.user.theme.Themer
import com.honzikv.androidlauncher.viewmodel.AppDrawerViewModel
import com.honzikv.androidlauncher.viewmodel.DockViewModel
import com.honzikv.androidlauncher.viewmodel.HomescreenViewModel
import com.honzikv.androidlauncher.viewmodel.SettingsViewModel
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
    single { get<LauncherDatabase>().userAppDao() }
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
            ), get()
        )
    }

    single { androidContext().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE) }

    single {
        FirstLaunchInitializer(
            get(), get(), androidContext().packageManager, get(), get()
        )
    }

    //Viewmodels
    viewModel { HomescreenViewModel(get(), androidContext().packageManager) }
    viewModel { AppDrawerViewModel(get(), get()) }
    viewModel { DockViewModel(get()) }
    viewModel { SettingsViewModel(get()) }

    single { Themer(get(), get()) }

}