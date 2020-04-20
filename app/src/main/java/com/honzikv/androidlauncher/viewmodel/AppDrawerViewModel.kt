package com.honzikv.androidlauncher.viewmodel

import com.honzikv.androidlauncher.data.repository.AppDrawerRepository

class AppDrawerViewModel(private val appDrawerRepository: AppDrawerRepository) {

    val appList = appDrawerRepository.getSystemApps()
}