package com.honzikv.androidlauncher.viewmodel

import androidx.lifecycle.*
import com.honzikv.androidlauncher.data.model.DrawerApp
import com.honzikv.androidlauncher.data.repository.AppDrawerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppDrawerViewModel(private val appDrawerRepository: AppDrawerRepository) : ViewModel() {

    private val appList = appDrawerRepository.getAppList()

    init {
        viewModelScope.launch(Dispatchers.Default) {
            appDrawerRepository.reloadAppList()
        }
    }

    fun getAppList() = appList

}