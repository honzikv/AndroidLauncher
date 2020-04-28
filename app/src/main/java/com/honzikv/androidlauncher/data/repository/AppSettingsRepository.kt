package com.honzikv.androidlauncher.data.repository

import android.content.SharedPreferences
import android.graphics.Color
import androidx.lifecycle.LiveData
import com.honzikv.androidlauncher.data.database.dao.ThemeProfileDao
import com.honzikv.androidlauncher.data.model.entity.ThemeProfileModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

const val APP_PREFERENCES = "userPreferences"
const val PREFS_INITIALIZED = "prefsInitialized"

const val FOLDER_COLS_COUNT_FIELD = "folderColsCount"
const val FOLDERS_COLS_COUNT_DEFAULT = 4

val DRAWER_COLOR_DEFAULT = Color.parseColor("#d7dbe0")

const val SHOW_DOCK_FIELD = "showDock"
val SHOW_DOCK_DEFAULT = true

const val MAX_FOLDERS_PER_PAGE = "maxFoldersPerPage"

const val SWIPE_DOWN_FOR_NOTIFICATION_PANEL_FIELD = "swipeDownForNotificationPanel"

const val USE_GRID_DRAWER_FIELD = "useGridDrawer"

const val ALWAYS_SHOW_FOLDER_CONTENT_FIELD = "alwaysShowFolderContent"

const val THEME_PROFILE_FIELD = "themeProfile"

const val DOCK_APP_LIMIT = 4;

class AppSettingsRepository(
    private val preferences: SharedPreferences,
    private val themeProfileDao: ThemeProfileDao
) {

    val currentThemeProfile: LiveData<ThemeProfileModel> =
        themeProfileDao.getSelectedProfileLiveData()

    val allProfiles: LiveData<List<ThemeProfileModel>> = themeProfileDao.getAllProfiles()

    suspend fun getCurrentThemeProfileSync(): ThemeProfileModel =
        themeProfileDao.getSelectedProfile()

    suspend fun setFolderColsCount(count: Int) {
        preferences.edit().apply {
            putInt(FOLDER_COLS_COUNT_FIELD, count)
            apply()
        }
    }

    suspend fun setMaxFoldersPerPage(count: Int) = withContext(Dispatchers.Default) {
        preferences.edit().apply {
            putInt(MAX_FOLDERS_PER_PAGE, count)
            apply()
        }
    }

    suspend fun setSwipeDownForNotificationPanel(enable: Boolean) =
        withContext(Dispatchers.Default) {
            preferences.edit().apply {
                putBoolean(SWIPE_DOWN_FOR_NOTIFICATION_PANEL_FIELD, enable)
                apply()
            }
        }

    suspend fun setShowDock(show: Boolean) = withContext(Dispatchers.Default) {
        preferences.edit().apply {
            putBoolean(SHOW_DOCK_FIELD, show)
            apply()
        }
    }

    fun setUseGridDrawer(use: Boolean) {
        preferences.edit().apply {
            putBoolean(USE_GRID_DRAWER_FIELD, use)
            apply()
        }
    }

    fun setAlwaysShowFolderContent(show: Boolean) {
        preferences.edit().apply {
            putBoolean(ALWAYS_SHOW_FOLDER_CONTENT_FIELD, show)
            apply()
        }
    }

    fun setThemeProfile(profileId: Long) {
        preferences.edit().apply {
            putLong(THEME_PROFILE_FIELD, profileId)
            apply()
        }
    }

    suspend fun addProfiles(profiles: List<ThemeProfileModel>) =
        themeProfileDao.addProfiles(profiles)

    suspend fun addProfile(profile: ThemeProfileModel): Long = themeProfileDao.addProfile(profile)

    suspend fun changeCurrentProfile(profile: ThemeProfileModel) =
        themeProfileDao.changeSelected(profile)

}
