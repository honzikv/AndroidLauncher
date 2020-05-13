package com.honzikv.androidlauncher.repository

import android.content.SharedPreferences
import android.graphics.Color
import com.honzikv.androidlauncher.util.booleanLiveData
import com.honzikv.androidlauncher.util.intLiveData
import timber.log.Timber
import java.lang.IllegalArgumentException

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

        const val SWIPE_DOWN_FOR_NOTIFICATION_PANEL_FIELD = "swipeDownForNotificationPanel"

        const val THEME_PROFILE_FIELD = "themeProfile"

        const val SHOW_SEARCH_BAR_FIELD = "showSearchBar"

        const val DOCK_ITEM_LIMIT_FIELD = "dockItemLimit"

        const val MAX_DOCK_ITEMS = 6

        const val DOCK_ITEM_LIMIT_DEFAULT = 4;

        const val USE_ONE_HANDED_MODE = "useOneHandedMode"

        const val SHOW_DRAWER_AS_GRID_FIELD = "showDrawerAsGrid"

        const val USE_ROUND_CORNERS_FIELD = "useRoundCorners"

        const val SHOW_DOCK_LABELS_FIELD = "showDockLabels"
    }

    val useRoundCorners = preferences.booleanLiveData(USE_ROUND_CORNERS_FIELD, true)
    fun getUseRoundCorners() = preferences.getBoolean(USE_ROUND_CORNERS_FIELD, true)
    fun setUseRoundCorners(use: Boolean) {
        preferences.edit().apply {
            putBoolean(USE_ROUND_CORNERS_FIELD, use)
            apply()
        }
    }

    val showDrawerAsGrid = preferences.booleanLiveData(SHOW_DRAWER_AS_GRID_FIELD, false)
    fun getShowDrawerAsGrid() = preferences.getBoolean(SHOW_DRAWER_AS_GRID_FIELD, false)
    fun setShowDrawerAsGrid(show: Boolean) {
        preferences.edit().apply {
            putBoolean(SHOW_DRAWER_AS_GRID_FIELD, show)
            apply()
        }
    }

    val swipeDownForNotifications =
        preferences.booleanLiveData(SWIPE_DOWN_FOR_NOTIFICATION_PANEL_FIELD, true)

    fun getSwipeDownForNotifications() = preferences.getBoolean(
        SWIPE_DOWN_FOR_NOTIFICATION_PANEL_FIELD, true
    )

    fun setSwipeDownForNotifications(enable: Boolean) {
        Timber.d("Setting swipe down for notifications to $enable")
        preferences.edit().apply {
            putBoolean(SWIPE_DOWN_FOR_NOTIFICATION_PANEL_FIELD, enable)
            apply()
            Timber.d("Swipe down for notifications enabled = $enable")
        }
    }

    val showDock = preferences.booleanLiveData(SHOW_DOCK_FIELD, true)
    fun getShowDock() = preferences.getBoolean(SHOW_DOCK_FIELD, true)
    fun setShowDock(show: Boolean) {
        preferences.edit().apply {
            putBoolean(SHOW_DOCK_FIELD, show)
            apply()
        }
    }

    val useOneHandedMode = preferences.booleanLiveData(USE_ONE_HANDED_MODE, false)
    fun getUseOneHandedMode() = preferences.getBoolean(USE_ONE_HANDED_MODE, false)
    fun setUseOneHandedMode(use: Boolean) {
        preferences.edit().apply {
            putBoolean(USE_ONE_HANDED_MODE, use)
            apply()
        }
    }

    val showSearchBar = preferences.booleanLiveData(SHOW_SEARCH_BAR_FIELD, true)
    fun getShowSearchBar() = preferences.getBoolean(SHOW_SEARCH_BAR_FIELD, true)
    fun setShowSearchBar(show: Boolean) {
        preferences.edit().apply {
            putBoolean(SHOW_SEARCH_BAR_FIELD, show)
            apply()
        }
    }


    val showDockLabels = preferences.booleanLiveData(SHOW_DOCK_LABELS_FIELD, false)
    fun getShowDockLabels() = preferences.getBoolean(SHOW_DOCK_LABELS_FIELD, false)
    fun setShowDockLabels(show: Boolean) {
        preferences.edit().apply {
            putBoolean(SHOW_DOCK_LABELS_FIELD, show)
            apply()
        }
    }

    val dockItemLimit = preferences.intLiveData(DOCK_ITEM_LIMIT_FIELD, DOCK_ITEM_LIMIT_DEFAULT)
    fun getDockItemLimit() = preferences.getInt(DOCK_ITEM_LIMIT_FIELD, DOCK_ITEM_LIMIT_DEFAULT)

    @Throws(IllegalArgumentException::class)
    fun setDockItemLimit(limit: Int) {
        if (limit <= 0 || limit > MAX_DOCK_ITEMS) {
            throw IllegalArgumentException(
                "Error number of items needs to be between 1 " +
                        "and $MAX_DOCK_ITEMS, you've entered $limit"
            )
        }
        preferences.edit().apply {
            putInt(DOCK_ITEM_LIMIT_FIELD, limit)
            apply()
        }
    }

}
