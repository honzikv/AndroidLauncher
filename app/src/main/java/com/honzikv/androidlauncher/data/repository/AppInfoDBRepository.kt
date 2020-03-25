package com.honzikv.androidlauncher.data.repository

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.honzikv.androidlauncher.data.database.AppInfoDatabase
import com.honzikv.androidlauncher.data.model.SystemAppModel

/**
 *
 */
class AppInfoDBRepository(application: Application) {

    private val appInfoDatabase = AppInfoDatabase.getInstance(application)

    private val systemApps = MutableLiveData<SystemAppModel>()


}