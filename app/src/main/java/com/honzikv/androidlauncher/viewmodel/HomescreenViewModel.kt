package com.honzikv.androidlauncher.viewmodel

import androidx.lifecycle.ViewModel
import com.honzikv.androidlauncher.data.repository.HomescreenRepository

class HomescreenViewModel(
    private val homescreenRepository: HomescreenRepository
) : ViewModel() {

    private var currentPage = 0

    private var totalPageCount: Int = homescreenRepository.getPageCount()


}