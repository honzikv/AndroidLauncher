package com.honzikv.androidlauncher.viewmodel

import androidx.lifecycle.*
import com.honzikv.androidlauncher.data.model.DrawerApp
import com.honzikv.androidlauncher.data.repository.AppDrawerRepository
import com.honzikv.androidlauncher.data.repository.AppSettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppDrawerViewModel(
    private val appDrawerRepository: AppDrawerRepository
) : ViewModel() {

    private val appList: MediatorLiveData<List<DrawerApp>> =
        MediatorLiveData<List<DrawerApp>>().apply {
            addSource(appDrawerRepository.getAppList()) { value = it }
            updateAppDrawerData()
        }

    private fun updateAppDrawerData() =
        viewModelScope.launch { appDrawerRepository.reloadAppList() }

    fun getDrawerApps() = appList

}