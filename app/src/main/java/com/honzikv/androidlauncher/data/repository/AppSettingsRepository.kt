package com.honzikv.androidlauncher.data.repository

import android.content.SharedPreferences
import android.graphics.Color
import androidx.lifecycle.LiveData
import com.honzikv.androidlauncher.data.database.dao.ThemeProfileDao
import com.honzikv.androidlauncher.data.model.entity.ThemeProfileModel
import com.honzikv.androidlauncher.data.sharedpreferences.booleanLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

const val APP_PREFERENCES = "userPreferences"
const val PREFS_INITIALIZED = "prefsInitialized"

const val FOLDER_COLS_COUNT_FIELD = "folderColsCount"
const val FOLDERS_COLS_COUNT_DEFAULT = 4


class AppSettingsRepository(
    private val preferences: SharedPreferences
) {

    companion object {

        val DRAWER_COLOR_DEFAULT = Color.parseColor("#d7dbe0")

        const val SHOW_DOCK_FIELD = "showDock"

        const val MAX_FOLDERS_PER_PAGE = "maxFoldersPerPage"

        const val SWIPE_DOWN_FOR_NOTIFICATION_PANEL_FIELD = "swipeDownForNotificationPanel"

        const val ALWAYS_SHOW_FOLDER_CONTENT_FIELD = "alwaysShowFolderContent"

        const val THEME_PROFILE_FIELD = "themeProfile"

        const val SHOW_SEARCH_BAR_FIELD = "showSearchBar"

        const val DOCK_APP_LIMIT = 4;

        const val USE_ONE_HANDED_MODE = "useOneHandedMode"

        const val SHOW_DRAWER_AS_GRID_FIELD = "showDrawerAsGrid"

        const val USE_ROUND_CORNERS_FIELD = "useRoundCorners"
    }


    val useRoundCorners = preferences.booleanLiveData(USE_ROUND_CORNERS_FIELD, true)
    val showDrawerAsGrid = preferences.booleanLiveData(SHOW_DRAWER_AS_GRID_FIELD, false)

    fun getSwipeDownForNotifications() =
        preferences.getBoolean(SWIPE_DOWN_FOR_NOTIFICATION_PANEL_FIELD, true)

    fun setSwipeDownForNotifications(enable: Boolean) {
        Timber.d("Setting swipe down for notifications to $enable")
        preferences.edit().apply {
            putBoolean(SWIPE_DOWN_FOR_NOTIFICATION_PANEL_FIELD, enable)
            apply()
            Timber.d("Swipe down for notifications enabled = $enable")
        }
    }

    fun getShowDock() = preferences.getBoolean(SHOW_DOCK_FIELD, true)

    suspend fun setShowDock(show: Boolean) = withContext(Dispatchers.Default) {
        preferences.edit().apply {
            putBoolean(SHOW_DOCK_FIELD, show)
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

    fun getUseOneHandedMode() = preferences.getBoolean(USE_ONE_HANDED_MODE, false)

    fun setUseOneHandedMode(use: Boolean) {
        preferences.edit().apply {
            putBoolean(USE_ONE_HANDED_MODE, use)
            apply()
        }
    }

    fun getShowSearchBar() = preferences.getBoolean(SHOW_SEARCH_BAR_FIELD, true)
    fun setShowSearchBar(show: Boolean) {
        preferences.edit().apply {
            putBoolean(SHOW_SEARCH_BAR_FIELD, show)
            apply()
        }
    }

    fun getShowDrawerAsGrid() = preferences.getBoolean(SHOW_DRAWER_AS_GRID_FIELD, false)
    fun setShowDrawerAsGrid(show: Boolean) {
        preferences.edit().apply {
            putBoolean(SHOW_DRAWER_AS_GRID_FIELD, show)
            apply()
        }
    }

    fun getUseRoundCorners() = preferences.getBoolean(USE_ROUND_CORNERS_FIELD, true)
    fun setUseRoundCorners(use: Boolean) {
        preferences.edit().apply {
            putBoolean(USE_ROUND_CORNERS_FIELD, use)
            apply()
        }
    }

}
