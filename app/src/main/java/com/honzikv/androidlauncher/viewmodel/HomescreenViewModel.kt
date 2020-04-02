package com.honzikv.androidlauncher.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.honzikv.androidlauncher.data.model.entity.FolderWithItems
import com.honzikv.androidlauncher.data.model.entity.FolderModel
import com.honzikv.androidlauncher.data.model.entity.PageModel
import com.honzikv.androidlauncher.data.repository.HomescreenRepository
import com.honzikv.androidlauncher.transformation.BackgroundTransformations

class HomescreenViewModel(
    private val homescreenRepository: HomescreenRepository
) : ViewModel() {

    val currentPageNumber = MutableLiveData(0)

    val totalPageCount =
        BackgroundTransformations.map(homescreenRepository.allPages, List<PageModel>::size)


    val currentPage =
        BackgroundTransformations.map(homescreenRepository.allPages) { list ->
            list[currentPageNumber.value!!]
        }

    val folderList: LiveData<List<FolderWithItems>> =
        BackgroundTransformations.map(homescreenRepository.allFolders) { list ->
            list[currentPageNumber.value!!].folderList
        }


    fun moveToNextPage() {
        if (currentPageNumber.value!! < totalPageCount.value!!) {
            currentPageNumber.postValue(currentPageNumber.value!! + 1)
        }
    }

    fun moveToPreviousPage() {
        if (currentPageNumber.value!! > 0) {
            currentPageNumber.postValue(currentPageNumber.value!! - 1)
        }
    }

}