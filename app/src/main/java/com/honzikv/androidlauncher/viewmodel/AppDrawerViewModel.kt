package com.honzikv.androidlauncher.viewmodel

import androidx.lifecycle.ViewModel
import com.honzikv.androidlauncher.data.repository.AppDrawerRepository

class AppDrawerViewModel(private val appDrawerRepository: AppDrawerRepository) : ViewModel() {

    val appList = appDrawerRepository.getSystemApps()
}