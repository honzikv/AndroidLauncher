package com.honzikv.androidlauncher.dependency

import androidx.room.Room
import com.honzikv.androidlauncher.data.database.LauncherDatabase
import com.honzikv.androidlauncher.data.preferences.UserSettings
import com.honzikv.androidlauncher.data.repository.DockDataRepository
import com.honzikv.androidlauncher.data.repository.FolderDataRepository
import com.honzikv.androidlauncher.data.repository.HomescreenPageRepository
import com.honzikv.androidlauncher.data.repository.SystemAppsRepository
import com.honzikv.androidlauncher.ui.viewmodel.HomescreenViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val repositoryModule = module {
    single { HomescreenPageRepository(get()) }
    single { DockDataRepository(get()) }
    single { FolderDataRepository(get()) }
    single { SystemAppsRepository(androidContext()) }
}

val databaseModule = module {
    single {
        Room.databaseBuilder(get(), LauncherDatabase::class.java, "app_database")
            .fallbackToDestructiveMigration()
            .build()
    }
}

val daoModule = module {
    single { get<LauncherDatabase>().dockDao() }
    single { get<LauncherDatabase>().folderDao() }
    single { get<LauncherDatabase>().homescreenPageDao() }
    single { get<LauncherDatabase>().userAppDao() }
}

val viewModelModule = module {
    viewModel { HomescreenViewModel(get(), get(), get()) }
}

val userSettingsModule = module {
    single { UserSettings() }
}