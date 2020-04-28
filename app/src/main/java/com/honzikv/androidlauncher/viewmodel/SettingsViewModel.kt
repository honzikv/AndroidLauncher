package com.honzikv.androidlauncher.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.honzikv.androidlauncher.data.model.entity.ThemeProfileModel
import com.honzikv.androidlauncher.data.repository.AppSettingsRepository
import com.honzikv.androidlauncher.data.repository.AppThemeRepository
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepository: AppSettingsRepository,
    private val appThemeRepository: AppThemeRepository
) : ViewModel() {

    val currentTheme = appThemeRepository.getCurrentTheme()

    val allThemes = appThemeRepository.allThemes

    fun changeTheme(theme: ThemeProfileModel) = viewModelScope.launch {
        appThemeRepository.changeTheme(theme)
    }

    fun getSwipeDownForNotifications() = settingsRepository.getSwipeDownForNotifications()
    fun setSwipeDownForNotifications(show: Boolean) = viewModelScope.launch {
        settingsRepository.setSwipeDownForNotifications(show)
    }

    fun getShowDock() = settingsRepository.getShowDock()
    fun setShowDock(show: Boolean) =
        viewModelScope.launch { settingsRepository.setShowDock(show) }

    fun getUseOneHandedMode() = settingsRepository.getUseOneHandedMode()
    fun setUseOneHandedMode(use: Boolean) = settingsRepository.setUseOneHandedMode(use)

}