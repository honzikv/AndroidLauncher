package com.honzikv.androidlauncher.viewmodel

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

    val swipeDownForNotifications = settingsRepository.swipeDownForNotifications
    fun setSwipeDownForNotifications(show: Boolean) = viewModelScope.launch {
        settingsRepository.setSwipeDownForNotifications(show)
    }

    val showDock = settingsRepository.showDock
    fun setShowDock(show: Boolean) =
        viewModelScope.launch { settingsRepository.setShowDock(show) }

    val useOneHandedMode = settingsRepository.useOneHandedMode
    fun setUseOneHandedMode(use: Boolean) = settingsRepository.setUseOneHandedMode(use)

    val showSearchBar = settingsRepository.showSearchBar
    fun setShowSearchBar(show: Boolean) = settingsRepository.setShowSearchBar(show)

    val showDrawerAsGrid = settingsRepository.showDrawerAsGrid
    fun getShowDrawerAsGrid() = settingsRepository.getShowDrawerAsGrid()
    fun setShowDrawerAsGrid(show: Boolean) = settingsRepository.setShowDrawerAsGrid(show)

    val useRoundCorners = settingsRepository.useRoundCorners
    fun setUseRoundCorners(use: Boolean) = settingsRepository.setUseRoundCorners(use)

    val showDockLabels = settingsRepository.showDockLabels
    fun setShowDockLabels(show: Boolean) = settingsRepository.setShowDockLabels(show)
    fun getUseRoundCorners() = settingsRepository.getUseRoundCorners()

    val dockItemLimit = settingsRepository.dockItemLimit
}