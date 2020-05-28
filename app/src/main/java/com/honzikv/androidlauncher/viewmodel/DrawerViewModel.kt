package com.honzikv.androidlauncher.viewmodel

import androidx.lifecycle.*
import com.honzikv.androidlauncher.model.DrawerApp
import com.honzikv.androidlauncher.repository.DrawerRepository
import com.honzikv.androidlauncher.repository.AppSettingsRepository
import com.honzikv.androidlauncher.repository.AppThemeRepository
import kotlinx.coroutines.launch

/**
 * ViewModel obsahujici seznam vsech aplikaci
 */
class DrawerViewModel(
    private val drawerRepository: DrawerRepository
) : ViewModel() {

    /**
     * Seznam vsech aplikaci
     */
    private val appList: MediatorLiveData<List<DrawerApp>> =
        MediatorLiveData<List<DrawerApp>>().apply {
            addSource(drawerRepository.getAppList()) { value = it }
            if (drawerRepository.getAppList().value?.isEmpty()!!) {
                updateAppDrawerData()
            }
        }

    /**
     * Aktualizuje seznam vsech aplikaci. Provede se ve vedlejsim vlakne.
     */
    private fun updateAppDrawerData() =
        viewModelScope.launch { drawerRepository.reloadAppList() }

    fun getDrawerApps(): LiveData<List<DrawerApp>> = appList

}