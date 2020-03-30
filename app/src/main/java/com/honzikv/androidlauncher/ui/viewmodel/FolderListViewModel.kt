package com.honzikv.androidlauncher.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.honzikv.androidlauncher.data.model.entity.FolderItemModel
import com.honzikv.androidlauncher.data.model.entity.FolderModel
import com.honzikv.androidlauncher.data.repository.FolderDataRepository

class FolderListViewModel(
    private val folderDataRepository: FolderDataRepository
) : ViewModel() {

    private val folderListPageMap: MutableMap<Int, MutableLiveData<List<FolderModel>>> =
        mutableMapOf()

    private val folderItemMap: MutableMap<Int, MutableLiveData<List<FolderItemModel>>> =
        mutableMapOf()

    fun getFolderList(pageNumber: Int): LiveData<List<FolderModel>> {
        if (!folderListPageMap.containsKey(pageNumber)) {
            folderListPageMap[pageNumber] = folderDataRepository.getFolderList(pageNumber)
        }

        return folderListPageMap[pageNumber] as LiveData<List<FolderModel>>
    }

    fun getFolderItems(folderId: Int) : LiveData<List<FolderItemModel>> {
        if (!folderItemMap.containsKey(folderId)) {
            folderItemMap[folderId] = folderDataRepository.getFolderItems(folderId)
        }

        return folderItemMap[folderId] as LiveData<List<FolderItemModel>>
    }
}