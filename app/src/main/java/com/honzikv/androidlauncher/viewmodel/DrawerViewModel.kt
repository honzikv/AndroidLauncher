package com.honzikv.androidlauncher.viewmodel

import androidx.lifecycle.*
import com.honzikv.androidlauncher.model.DrawerApp
import com.honzikv.androidlauncher.repository.DrawerRepository
import com.honzikv.androidlauncher.repository.AppSettingsRepository
import com.honzikv.androidlauncher.repository.AppThemeRepository
import kotlinx.coroutines.launch

class DrawerViewModel(
    private val drawerRepository: DrawerRepository,
    appThemeRepository: AppThemeRepository,
    private val appSettingsRepository: AppSettingsRepository
) : ViewModel() {

    /**
     * AppList is a mediator live data that subscribes to repository live data and updates it
     * to ensure all apps are loaded, repository live data is also being updated by system package
     * changes - e.g when new app is installed the repository is notified and updates the livedata
     */
    private val appList: MediatorLiveData<List<DrawerApp>> =
        MediatorLiveData<List<DrawerApp>>().apply {
            addSource(drawerRepository.getAppList()) { value = it }
            if (drawerRepository.getAppList().value?.isEmpty()!!) {
                updateAppDrawerData()
            }
        }

    private fun updateAppDrawerData() =
        viewModelScope.launch { drawerRepository.reloadAppList() }

    fun getDrawerApps(): LiveData<List<DrawerApp>> = appList

}