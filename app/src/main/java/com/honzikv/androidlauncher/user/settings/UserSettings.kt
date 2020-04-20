package com.honzikv.androidlauncher.user.settings

import android.content.SharedPreferences

const val APP_PREFERENCES = "userPreferences"
const val PREFS_INITIALIZED = "prefsInitialized"

const val FOLDER_COLS_COUNT_FIELD = "folderColsCount"
const val FOLDER_COLS_COUNT = 4

class UserSettings(private val preferences: SharedPreferences) {

    fun getFolderColsCount() = preferences.getInt(FOLDER_COLS_COUNT_FIELD, FOLDER_COLS_COUNT)


}