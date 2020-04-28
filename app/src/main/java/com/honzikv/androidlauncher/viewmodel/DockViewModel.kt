package com.honzikv.androidlauncher.viewmodel

import androidx.lifecycle.ViewModel
import com.honzikv.androidlauncher.data.repository.DockRepository

class DockViewModel(
    private val dockRepository: DockRepository
) : ViewModel() {

    val dockItems = dockRepository.dockItems

    fun addItem(packageName: String) = dockRepository.addItem(packageName)
}