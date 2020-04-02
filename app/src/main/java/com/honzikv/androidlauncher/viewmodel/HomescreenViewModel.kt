package com.honzikv.androidlauncher.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.honzikv.androidlauncher.data.model.entity.FolderItemModel
import com.honzikv.androidlauncher.data.model.entity.FolderModel
import com.honzikv.androidlauncher.data.model.entity.PageModel
import com.honzikv.androidlauncher.data.repository.HomescreenRepository
import kotlinx.coroutines.*

class HomescreenViewModel(
    private val homescreenRepository: HomescreenRepository
) : ViewModel() {

    private var currentPage = 0

    private var totalPageCount = homescreenRepository.getPageCount()

    private var pageObservable = MutableLiveData<PageModel>()

    private var pageFolders: LiveData<List<FolderModel>>? = null

    init {

    }

    private fun refreshPage() = homescreenRepository.getPage(currentPage)

    private fun refreshPageFolders() = homescreenRepository.getFolders(currentPage)

    fun getPage(): LiveData<PageModel> {
        if (page == null || page?.value?.pageNumber != currentPage) {
            page = refreshPage()
            pageFolders = refreshPageFolders()
        }

        return page as LiveData<PageModel>
    }

    fun getFolders(): LiveData<List<FolderModel>> {
        if (pageFolders == null) {
            pageFolders = refreshPageFolders()
        }

        return pageFolders as LiveData<List<FolderModel>>
    }

    fun moveToNextPage() {
        if (currentPage < totalPageCount) {
            currentPage = currentPage.plus(1)
            page = refreshPage()
            pageFolders = refreshPageFolders()
        }
    }

    fun removeItemFromFolder(item: FolderItemModel, folder: FolderModel) {
        homescreenRepository.removeItemFromFolder(item, folder)
    }
}