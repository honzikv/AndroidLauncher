package com.honzikv.androidlauncher.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.honzikv.androidlauncher.data.model.entity.PageFolderList
import com.honzikv.androidlauncher.data.model.entity.PageModel
import com.honzikv.androidlauncher.data.repository.HomescreenRepository

class HomescreenViewModel(
    private val homescreenRepository: HomescreenRepository
) : ViewModel() {

    private var currentPageNumber = MutableLiveData(0)

    private var totalPageCount =
        Transformations.map(homescreenRepository.getAllPages(), List<PageModel>::size)

    private val currentPage = Transformations.map(homescreenRepository.getAllPages()) { list ->
        list[currentPageNumber.value!!]
    }

    private val folderList: LiveData<PageFolderList> =
        Transformations.map(homescreenRepository.getAllFolders()) { list ->
            list[currentPageNumber.value!!]
        }


}