package com.honzikv.androidlauncher.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.honzikv.androidlauncher.data.model.FolderModel
import com.honzikv.androidlauncher.data.repository.HomescreenRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FolderSettingsViewModel(private val homescreenRepository: HomescreenRepository) :
    ViewModel() {

        fun updateFolder(folderModel: FolderModel) {
            viewModelScope.launch {
                homescreenRepository.updateFolder(folderModel)
            }
        }

        fun deleteFolder(folderModel: FolderModel) {
            viewModelScope.launch {
                homescreenRepository.deleteFolder(folderModel)
            }
        }
    }