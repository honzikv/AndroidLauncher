package com.honzikv.androidlauncher.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.honzikv.androidlauncher.data.model.DrawerAppModel
import com.honzikv.androidlauncher.data.model.entity.FolderModel
import com.honzikv.androidlauncher.data.repository.FolderDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FolderListViewModel(
    private val folderDataRepository: FolderDataRepository,
    private val appContext: Context
) : ViewModel() {

    private val folderListPageMap: MutableMap<>
}