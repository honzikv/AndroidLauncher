package com.honzikv.androidlauncher.viewmodel

import android.content.pm.PackageManager
import androidx.lifecycle.*
import com.honzikv.androidlauncher.data.repository.HomescreenRepository

class HomescreenViewModel(
    private val homescreenRepository: HomescreenRepository,
    private val packageManager: PackageManager
) : ViewModel() {

    private val currentPageNumber: MutableLiveData<Int> = MutableLiveData(0)

    val allPages = homescreenRepository.allPages

    val items = homescreenRepository.allItems

}