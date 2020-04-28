package com.honzikv.androidlauncher.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.honzikv.androidlauncher.data.model.entity.ThemeProfileDto
import com.honzikv.androidlauncher.data.repository.AppSettingsRepository

class SettingsViewModel(private val settingsRepository: AppSettingsRepository) : ViewModel() {

    private val currentThemeProfile: LiveData<ThemeProfileDto> by lazy {
        MutableLiveData<ThemeProfileDto>().also {

        }
    }
}