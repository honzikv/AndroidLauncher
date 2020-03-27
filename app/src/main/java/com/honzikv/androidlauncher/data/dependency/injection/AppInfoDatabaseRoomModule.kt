package com.honzikv.androidlauncher.data.dependency.injection

import android.app.Application
import androidx.room.Room
import com.honzikv.androidlauncher.data.database.AppInfoDatabase
import com.honzikv.androidlauncher.data.database.dao.DockDao
import com.honzikv.androidlauncher.data.database.dao.FolderDao
import com.honzikv.androidlauncher.data.database.dao.UserAppDao
import com.honzikv.androidlauncher.data.repository.AppDataRepository
import dagger.Provides
import javax.inject.Singleton

class AppInfoDatabaseRoomModule(application: Application?) {
    private val demoDatabase: AppInfoDatabase = Room.databaseBuilder(
        application!!,
        AppInfoDatabase::class.java,
        "demo-db"
    ).build()

    @Singleton
    @Provides
    fun provideRoomDatabase(): AppInfoDatabase = demoDatabase

    @Singleton
    @Provides
    fun provideUserAppDao(appInfoDatabase: AppInfoDatabase) = appInfoDatabase.userAppDao()

    @Singleton
    @Provides
    fun provideDockDao(appInfoDatabase: AppInfoDatabase) = appInfoDatabase.dockDao()

    @Singleton
    @Provides
    fun provideFolderDao(appInfoDatabase: AppInfoDatabase) = appInfoDatabase.folderDao()

    fun provideAppDataRepository(userAppDao: UserAppDao, dockDao: DockDao, folderDao: FolderDao) =
        AppDataRepository(userAppDao, folderDao, dockDao)

}