package com.honzikv.androidlauncher.injection

import android.content.Context
import androidx.room.Room
import com.honzikv.androidlauncher.data.database.LauncherDatabase
import com.honzikv.androidlauncher.data.first.launch.FirstLaunchInitializer
import com.honzikv.androidlauncher.data.repository.DockRepository
import com.honzikv.androidlauncher.data.repository.FolderDataRepository
import com.honzikv.androidlauncher.data.repository.HomescreenRepository
import com.honzikv.androidlauncher.data.repository.AppDrawerRepository
import com.honzikv.androidlauncher.user.settings.APP_PREFERENCES
import com.honzikv.androidlauncher.user.settings.UserSettings
import com.honzikv.androidlauncher.viewmodel.HomescreenViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val module = module {

    single {
        Room.databaseBuilder(get(), LauncherDatabase::class.java, "app_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    single { get<LauncherDatabase>().dockDao() }
    single { get<LauncherDatabase>().folderDao() }
    single { get<LauncherDatabase>().homescreenPageDao() }
    single { get<LauncherDatabase>().userAppDao() }

    single { HomescreenRepository(get(), get()) }
    single { DockRepository(get()) }
    single { FolderDataRepository(get()) }
    single { AppDrawerRepository(androidContext().packageManager) }

    single { androidContext().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE) }

    single {
        FirstLaunchInitializer(
            get(), get(), androidContext().packageManager, get()
        )
    }

    viewModel { HomescreenViewModel(get(), androidContext().packageManager) }

    single {
        UserSettings(
            androidContext().getSharedPreferences(
                APP_PREFERENCES,
                Context.MODE_PRIVATE
            )
        )
    }

}