package com.honzikv.androidlauncher.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.honzikv.androidlauncher.model.ThemeProfileModel
import com.honzikv.androidlauncher.repository.AppSettingsRepository
import com.honzikv.androidlauncher.repository.AppThemeRepository
import kotlinx.coroutines.launch

/**
 * View model pro nastaveni aplikace
 */
class SettingsViewModel(
    private val settingsRepository: AppSettingsRepository,
    private val appThemeRepository: AppThemeRepository
) : ViewModel() {

    /**
     * LiveData s aktualnim motivem barev
     */
    val currentTheme = appThemeRepository.getCurrentTheme()

    /**
     * Vsechny temata aplikace
     */
    val allThemes = appThemeRepository.allThemes

    /**
     * Zmena temata. Provede se ve vedlejsim vlakne
     */
    fun changeTheme(theme: ThemeProfileModel) = viewModelScope.launch {
        appThemeRepository.changeTheme(theme)
    }

    /**
     * Prida vsechny profily z argumentu funkce. Provede se ve vedlejsim vlakne
     */
    fun addProfile(vararg profile: ThemeProfileModel) = viewModelScope.launch {
        appThemeRepository.addProfile(*profile)
    }

    /**
     * Swipe down gesto pro zobrazeni notifikaci - LiveData, getter a setter
     */
    val swipeDownForNotifications = settingsRepository.swipeDownForNotifications
    fun getSwipeDownForNotifications() = settingsRepository.getSwipeDownForNotifications()
    fun setSwipeDownForNotifications(show: Boolean) {
        settingsRepository.setSwipeDownForNotifications(show)
    }

    /**
     * Zobrazeni doku na plose - LiveData, getter a setter
     */
    val showDock = settingsRepository.showDock
    fun getShowDock() = settingsRepository.getShowDock()
    fun setShowDock(show: Boolean) = viewModelScope.launch { settingsRepository.setShowDock(show) }

    /**
     * Zobrazeni vyhledavani v draweru - LiveData, getter a setter
     */
    val showSearchBar = settingsRepository.showSearchBar
    fun getShowSearchBar() = settingsRepository.getShowSearchBar()
    fun setShowSearchBar(show: Boolean) = settingsRepository.setShowSearchBar(show)

    /**
     * Zobrazeni aplikaci v draweru ve mrizce - misto linearniho seznamu - LiveData, getter a setter
     */
    val showDrawerAsGrid = settingsRepository.showDrawerAsGrid
    fun getShowDrawerAsGrid() = settingsRepository.getShowDrawerAsGrid()
    fun setShowDrawerAsGrid(show: Boolean) = settingsRepository.setShowDrawerAsGrid(show)

    /**
     * Pouziti zakulacenych rohu u draweru - LiveData, getter a setter
     */
    val useRoundCorners = settingsRepository.useRoundCorners
    fun setUseRoundCorners(use: Boolean) = settingsRepository.setUseRoundCorners(use)
    fun getUseRoundCorners() = settingsRepository.getUseRoundCorners()

    /**
     * Zobrazeni popisku ikon v doku - LiveData, getter a setter
     */
    val showDockLabels = settingsRepository.showDockLabels
    fun setShowDockLabels(show: Boolean) = settingsRepository.setShowDockLabels(show)
    fun getShowDockLabels() = settingsRepository.getShowDockLabels()

    /**
     * Zobrazeni tecek pro indikaci stranky - LiveData, getter a setter
     */
    val showPageDots = settingsRepository.showPageDots
    fun getShowPageDots(): Boolean = settingsRepository.getShowPageDots()
    fun setShowPageDots(show: Boolean) = settingsRepository.setShowPageDots(show)
}