package com.honzikv.androidlauncher.data.dependency.injection

import android.app.Application
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(var application: Application) {

    @Singleton
    @Provides
    fun providesApplication(): Application {
        return application
    }

}