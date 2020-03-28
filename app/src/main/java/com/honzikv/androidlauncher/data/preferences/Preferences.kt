package com.honzikv.androidlauncher.data.preferences

import android.app.Application
import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.get

const val PREFERENCES_NAME = "userPreferences"

const val PREFS_INITIALIZED = "prefsInitialized"

const val DOCK_MAX_SLOTS_FIELD = "dockMaxSlots"
const val DOCK_MAX_SLOTS = 4

object Preferences : KoinComponent {

    val application: Application = get()

    suspend fun createUserPreferences() {
        withContext(Dispatchers.IO) {
            val preferences = application.getSharedPreferences(PREFERENCES_NAME, 0)
            val editor = preferences.edit()

            if (!preferences.contains(PREFS_INITIALIZED)) {
                createDefaultPreferences(editor)
            }
        }
    }

    private suspend fun createDefaultPreferences(editor: SharedPreferences.Editor) {
        editor.putInt(DOCK_MAX_SLOTS_FIELD, DOCK_MAX_SLOTS)

    }
}