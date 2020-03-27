package com.honzikv.androidlauncher.data.dependency.injection

import android.app.LauncherActivity
import com.honzikv.androidlauncher.data.database.AppInfoDatabase
import com.honzikv.androidlauncher.data.repository.AppDataRepository
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [AppModule::class, AppInfoDatabaseRoomModule::class])
interface AppComponent {
    fun inject(mainActivity: LauncherActivity) {

    }

    fun appDao() : AppDao

    fun appInfoDatabase() : AppInfoDatabase

    fun appInfoRepository() : AppDataRepository
}