package com.honzikv.androidlauncher.repository

import android.content.SharedPreferences
import com.honzikv.androidlauncher.utils.booleanLiveData
import com.honzikv.androidlauncher.utils.intLiveData
import timber.log.Timber
import java.lang.IllegalArgumentException

const val APP_PREFERENCES = "userPreferences"
const val PREFS_INITIALIZED = "prefsInitialized"

/**
 * Repository poskytujici funkce pro upravu nastaveni aplikace. Nastaveni se uklada do SharedPreferences,
 * protoze nabizi nejsnazsi zpusob jak data ziskat a ulozit kdekoliv v aplikaci.
 * Jednotlive funkce get / set slouzi k ziskani a nastaveni hodnot z companion objektu. Jednotlive
 * promenne ve tride jsou jako LiveData pro real-time aktualizaci
 */
class AppSettingsRepository(
    private val preferences: SharedPreferences
) {

    companion object {

        /**
         * Zobrazeni doku na plose
         */
        const val SHOW_DOCK_FIELD = "showDock"

        /**
         * Tazeni dolu pro zobrazeni panelu s notifikacemi
         */
        const val SWIPE_DOWN_FOR_NOTIFICATION_PANEL_FIELD = "swipeDownForNotificationPanel"

        /**
         * Zobrazeni vyhledavani v draweru
         */
        const val SHOW_SEARCH_BAR_FIELD = "showSearchBar"

        /**
         * Zobrazeni aplikaci ve mrizce
         */
        const val SHOW_DRAWER_AS_GRID_FIELD = "showDrawerAsGrid"

        /**
         * Pouziti kulatych rohu pro drawer aplikace
         */
        const val USE_ROUND_CORNERS_FIELD = "useRoundCorners"

        /**
         * Zobrazeni popisku aplikaci v doku
         */
        const val SHOW_DOCK_LABELS_FIELD = "showDockLabels"

        /**
         * Zobrazeni navigace stranek teckami
         */
        const val SHOW_PAGE_DOTS = "showPageDots"
    }

    val useRoundCorners = preferences.booleanLiveData(USE_ROUND_CORNERS_FIELD, true)
    fun getUseRoundCorners() = preferences.getBoolean(USE_ROUND_CORNERS_FIELD, true)
    fun setUseRoundCorners(use: Boolean) {
        Timber.d("Setting use round corners to $use")
        preferences.edit().apply {
            putBoolean(USE_ROUND_CORNERS_FIELD, use)
            apply()
        }
    }

    val showDrawerAsGrid = preferences.booleanLiveData(SHOW_DRAWER_AS_GRID_FIELD, false)
    fun getShowDrawerAsGrid() = preferences.getBoolean(SHOW_DRAWER_AS_GRID_FIELD, false)
    fun setShowDrawerAsGrid(show: Boolean) {
        Timber.d("Setting show drawer as grid to $show")
        preferences.edit().apply {
            putBoolean(SHOW_DRAWER_AS_GRID_FIELD, show)
            apply()
        }
    }

    val swipeDownForNotifications =
        preferences.booleanLiveData(SWIPE_DOWN_FOR_NOTIFICATION_PANEL_FIELD, true)

    fun getSwipeDownForNotifications() =
        preferences.getBoolean(SWIPE_DOWN_FOR_NOTIFICATION_PANEL_FIELD, true)

    fun setSwipeDownForNotifications(enable: Boolean) {
        Timber.d("Setting swipe down for notifications to $enable")
        preferences.edit().apply {
            putBoolean(SWIPE_DOWN_FOR_NOTIFICATION_PANEL_FIELD, enable)
            apply()
        }
    }

    val showDock = preferences.booleanLiveData(SHOW_DOCK_FIELD, true)
    fun getShowDock() = preferences.getBoolean(SHOW_DOCK_FIELD, true)
    fun setShowDock(show: Boolean) {
        Timber.d("Setting show dock to $show")
        preferences.edit().apply {
            putBoolean(SHOW_DOCK_FIELD, show)
            apply()
        }
    }

    val showSearchBar = preferences.booleanLiveData(SHOW_SEARCH_BAR_FIELD, true)
    fun getShowSearchBar() = preferences.getBoolean(SHOW_SEARCH_BAR_FIELD, true)
    fun setShowSearchBar(show: Boolean) {
        Timber.d("Setting show search bar to $show")
        preferences.edit().apply {
            putBoolean(SHOW_SEARCH_BAR_FIELD, show)
            apply()
        }
    }

    val showDockLabels = preferences.booleanLiveData(SHOW_DOCK_LABELS_FIELD, false)
    fun getShowDockLabels() = preferences.getBoolean(SHOW_DOCK_LABELS_FIELD, false)
    fun setShowDockLabels(show: Boolean) {
        Timber.d("Setting show dock labels to $show")
        preferences.edit().apply {
            putBoolean(SHOW_DOCK_LABELS_FIELD, show)
            apply()
        }
    }

    val showPageDots = preferences.booleanLiveData(SHOW_PAGE_DOTS, true)
    fun getShowPageDots() = preferences.getBoolean(SHOW_PAGE_DOTS, true)
    fun setShowPageDots(show: Boolean) {
        Timber.d("Setting show page dots to $show")
        preferences.edit().apply {
            putBoolean(SHOW_PAGE_DOTS, show)
            apply()
        }
    }

}
