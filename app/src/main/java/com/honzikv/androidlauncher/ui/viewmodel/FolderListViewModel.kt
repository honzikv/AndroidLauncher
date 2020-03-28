package com.honzikv.androidlauncher.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.honzikv.androidlauncher.data.model.LauncherAppModel
import com.honzikv.androidlauncher.data.model.entity.FolderModel
import com.honzikv.androidlauncher.data.repository.FolderDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FolderListViewModel(
    private val folderDataRepository: FolderDataRepository,
    private val appContext: Context
) : ViewModel() {

    private var folderMap: Map<Int, LiveData<List<FolderModel>>> = mutableMapOf()

    private var folderToItemMap: Map<FolderModel, LiveData<List<LauncherAppModel>>> =
        mutableMapOf()

    /**
     * Returns list of all folders for current page in HomescreenViewModel
     */
    suspend fun getFolders(page: Int): LiveData<List<FolderModel>>? {
        if (!folderMap.containsKey(page)) {
            withContext(Dispatchers.IO) {
                folderMap[page] = MutableLiveData()
            }
        }

        return folderMap.get(page)
    }
}