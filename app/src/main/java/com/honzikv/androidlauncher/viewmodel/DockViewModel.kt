package com.honzikv.androidlauncher.viewmodel

import com.honzikv.androidlauncher.data.repository.DockRepository

class DockViewModel(
    private val dockRepository: DockRepository
) {

    val dock = dockRepository.dock

    val dockItems = dockRepository.dockItems

    fun addItem(packageName: String) = dockRepository.addItem(packageName)
}