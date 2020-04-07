package com.honzikv.androidlauncher.data.first.launch

import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import com.honzikv.androidlauncher.data.model.entity.FolderDto
import com.honzikv.androidlauncher.data.model.entity.FolderItemDto
import com.honzikv.androidlauncher.data.model.entity.PageDto
import com.honzikv.androidlauncher.data.repository.FolderDataRepository
import com.honzikv.androidlauncher.data.repository.HomescreenRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import timber.log.Timber

val DEFAULT_PACKAGES =
    arrayOf(
        "com.google.android.gm",
        "com.google.android.chrome",
        "com.google.android.youtube",
        "com.google.android.apps.calendar",
        "com.google.android.apps.photos",
        "com.google.android.apps.translate"
    )

const val FOLDER_COLOR = Color.WHITE
const val FOLDER_NAME = "Google Apps"

const val APP_PREFERENCES = "userPreferences"
const val PREFS_INITIALIZED = "prefsInitialized"

/**
 * Class that creates database objects for the first launch
 */
class FirstLaunchInitializer(
    private val folderDataRepository: FolderDataRepository,
    private val homescreenRepository: HomescreenRepository,
    private val packageManager: PackageManager,
    private val sharedPreferences: SharedPreferences
) {

    fun isAppInitialized(): Boolean {
        return sharedPreferences.getBoolean(PREFS_INITIALIZED, false)
    }

    suspend fun initialize() = withContext(Dispatchers.IO) {
        Timber.i("Initializing default settings")
        val pageDeferred = async { createFirstPage() }
        val folderDeferred = async { createGoogleFolder() }
        homescreenRepository.addFolderToPage(pageDeferred.await(), folderDeferred.await())
        commitInitialized()
    }

    private fun commitInitialized() {
        val editor = sharedPreferences.edit()
        editor.putBoolean(PREFS_INITIALIZED, true)
        editor.apply()
        Timber.d("Successfully set default variables")
    }

    private suspend fun createFirstPage(): Long {
        return homescreenRepository.addPageAsLast(PageDto())
    }

    /**
     * Creates folder_header with google apps
     */
    private suspend fun createGoogleFolder(): Long = withContext(Dispatchers.IO) {

        val folderId = folderDataRepository.addFolder(
            FolderDto(null, null, null, FOLDER_COLOR, FOLDER_NAME)
        )

        val appList = mutableListOf<FolderItemDto>()
        DEFAULT_PACKAGES.forEach { appPackage ->
            if (isAppInstalled(appPackage)) {
                appList.add(FolderItemDto(null, folderId, appPackage))
            }
        }

        val folder = folderDataRepository.getFolder(folderId)

        appList.forEach { folderItem ->
            folderDataRepository.addAppToFolder(folderItem, folder)
        }

        return@withContext folderId
    }

    private fun isAppInstalled(packageName: String): Boolean {
        return try {
            packageManager.getApplicationInfo(packageName, 0).enabled
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

}
