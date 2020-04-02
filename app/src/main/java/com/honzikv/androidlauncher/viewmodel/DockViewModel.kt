package com.honzikv.androidlauncher.viewmodel

import com.honzikv.androidlauncher.data.repository.DockDataRepository

class DockViewModel(
    private val dockDataRepository: DockDataRepository
) {

    val dock = dockDataRepository.dock

    val dockItems = dockDataRepository.dockItems

    fun addItem()
}