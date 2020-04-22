package com.honzikv.androidlauncher.viewmodel

import android.content.pm.PackageManager
import androidx.lifecycle.*
import com.honzikv.androidlauncher.data.model.entity.FolderWithItems
import com.honzikv.androidlauncher.data.model.entity.PageDto
import com.honzikv.androidlauncher.data.model.entity.PageWithFolders
import com.honzikv.androidlauncher.data.repository.HomescreenRepository
import com.honzikv.androidlauncher.transformation.BackgroundTransformations
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomescreenViewModel(
    private val homescreenRepository: HomescreenRepository,
    private val packageManager: PackageManager
) : ViewModel() {

    private val currentPageNumber: MutableLiveData<Int> = MutableLiveData(0)

    val totalPageCount: LiveData<Int> =
        BackgroundTransformations.map(homescreenRepository.allPages, List<PageWithFolders>::size)

    /**
     * Folder list for [currentPageNumber]
     */
    val folderList: LiveData<List<FolderWithItems>> =
        BackgroundTransformations.map(homescreenRepository.allPages) { list ->
            //Return list with folders of current page
            list[currentPageNumber.value!!].folderList
                .apply { loadItemInfo() }
        }

    private fun List<FolderWithItems>.loadItemInfo() {
        forEach { folder ->
            folder.itemList.forEach { item ->
                //If drawable is null load it
                if (item.drawable == null || item.label == null) {
                    try {
                        //Might throw NameNotFoundException
                        val appInfo =
                            packageManager.getApplicationInfo(
                                item.systemAppPackageName,
                                0
                            )
                        item.drawable = packageManager.getDrawable(
                            item.systemAppPackageName,
                            0,
                            appInfo
                        )
                        item.label =
                            packageManager.getApplicationLabel(appInfo).toString()
                        //App might have been uninstalled so remove item from db
                    } catch (nameNotFoundEx: PackageManager.NameNotFoundException) {
                        CoroutineScope(Dispatchers.IO).launch {
                            homescreenRepository.removeItem(item)
                        }
                    }
                }
            }
        }
    }

    suspend fun moveToNextPage() {
        if (currentPageNumber.value!! < totalPageCount.value!!) {
            currentPageNumber.postValue(currentPageNumber.value!! + 1)
        }
    }

    suspend fun moveToPreviousPage() {
        if (currentPageNumber.value!! > 0) {
            currentPageNumber.postValue(currentPageNumber.value!! - 1)
        }
    }

    suspend fun removePage(page: PageDto) {
        homescreenRepository.removePage(page)
    }

    /**
     * Page number is kept as private property so it cannot be modified when accessed by name
     */
    private fun getCurrentPageNumber(): LiveData<Int> {
        return currentPageNumber as LiveData<Int>
    }


}