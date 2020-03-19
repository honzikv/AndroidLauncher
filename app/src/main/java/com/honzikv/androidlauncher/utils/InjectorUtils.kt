package com.honzikv.androidlauncher.utils

import com.honzikv.androidlauncher.data.SystemDataRepository
import com.honzikv.androidlauncher.ui.viewmodel.SystemDataViewModelFactory

/**
 * Simple dependency injector
 */
object InjectorUtils {

    fun getSystemDataViewModelFactory(): SystemDataViewModelFactory {
        return SystemDataViewModelFactory(SystemDataRepository.getInstance())
    }
}