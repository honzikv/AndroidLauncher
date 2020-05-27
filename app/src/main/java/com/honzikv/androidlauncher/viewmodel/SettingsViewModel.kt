package com.honzikv.androidlauncher.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.honzikv.androidlauncher.model.ThemeProfileModel
import com.honzikv.androidlauncher.repository.AppSettingsRepository
import com.honzikv.androidlauncher.repository.AppThemeRepository
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

    fun addProfile(vararg profile: ThemeProfileModel) = viewModelScope.launch {
        appThemeRepository.addProfile(*profile)
    }

    val swipeDownForNotifications = settingsRepository.swipeDownForNotifications
    fun getSwipeDownForNotifications() = settingsRepository.getSwipeDownForNotifications()
    fun setSwipeDownForNotifications(show: Boolean) {
        settingsRepository.setSwipeDownForNotifications(show)
    }

    val showDock = settingsRepository.showDock
    fun getShowDock() = settingsRepository.getShowDock()
    fun setShowDock(show: Boolean) = viewModelScope.launch { settingsRepository.setShowDock(show) }

    val showSearchBar = settingsRepository.showSearchBar
    fun getShowSearchBar() = settingsRepository.getShowSearchBar()
    fun setShowSearchBar(show: Boolean) = settingsRepository.setShowSearchBar(show)

    val showDrawerAsGrid = settingsRepository.showDrawerAsGrid
    fun getShowDrawerAsGrid() = settingsRepository.getShowDrawerAsGrid()
    fun setShowDrawerAsGrid(show: Boolean) = settingsRepository.setShowDrawerAsGrid(show)

    val useRoundCorners = settingsRepository.useRoundCorners
    fun setUseRoundCorners(use: Boolean) = settingsRepository.setUseRoundCorners(use)
    fun getUseRoundCorners() = settingsRepository.getUseRoundCorners()

    val showDockLabels = settingsRepository.showDockLabels
    fun setShowDockLabels(show: Boolean) = settingsRepository.setShowDockLabels(show)
    fun getShowDockLabels() = settingsRepository.getShowDockLabels()

    val showPageDots = settingsRepository.showPageDots
    fun getShowPageDots(): Boolean = settingsRepository.getShowPageDots()
    fun setShowPageDots(show: Boolean) = settingsRepository.setShowPageDots(show)
}