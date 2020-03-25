package com.honzikv.androidlauncher.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.honzikv.androidlauncher.data.repository.SystemDataRepository

class SystemDataViewModelFactory(private val systemDataRepository: SystemDataRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SystemDataViewModel(systemDataRepository) as T
    }
}