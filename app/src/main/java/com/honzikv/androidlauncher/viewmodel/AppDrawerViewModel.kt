package com.honzikv.androidlauncher.viewmodel

import androidx.lifecycle.*
import com.honzikv.androidlauncher.data.model.DrawerApp
import com.honzikv.androidlauncher.data.repository.AppDrawerRepository
import com.honzikv.androidlauncher.data.repository.AppSettingsRepository
import com.honzikv.androidlauncher.data.repository.AppThemeRepository
import kotlinx.coroutines.launch

class AppDrawerViewModel(
    private val appDrawerRepository: AppDrawerRepository,
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
            addSource(appDrawerRepository.getAppList()) { value = it }
            updateAppDrawerData()
        }

    val currentTheme = appThemeRepository.getCurrentTheme()

    val useRoundCorners = appSettingsRepository.useRoundCorners

    val showDrawerAsGrid = appSettingsRepository.showDrawerAsGrid

    private fun updateAppDrawerData() =
        viewModelScope.launch { appDrawerRepository.reloadAppList() }

    fun getDrawerApps(): LiveData<List<DrawerApp>> = appList

}