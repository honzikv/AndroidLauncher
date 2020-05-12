package com.honzikv.androidlauncher.initializer

import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import com.google.gson.Gson
import com.honzikv.androidlauncher.data.model.FolderItemModel
import com.honzikv.androidlauncher.data.model.FolderModel
import com.honzikv.androidlauncher.data.model.PageModel
import com.honzikv.androidlauncher.data.model.ThemeProfileModel
import com.honzikv.androidlauncher.data.repository.AppSettingsRepository
import com.honzikv.androidlauncher.data.repository.PREFS_INITIALIZED
import com.honzikv.androidlauncher.viewmodel.HomescreenViewModel
import com.honzikv.androidlauncher.viewmodel.SettingsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class Initializer(
    private val homescreenViewModel: HomescreenViewModel,
    private val settingsViewModel: SettingsViewModel,
    private val sharedPreferences: SharedPreferences,
    private val packageManager: PackageManager
) {

    companion object {
        val DEFAULT_PACKAGES =
            arrayOf(
                "com.google.android.gm",
                "com.google.android.chrome",
                "com.google.android.youtube",
                "com.google.android.apps.calendar",
                "com.google.android.apps.photos",
                "com.google.android.apps.translate"
            )


        const val FOLDER_COLOR = Color.BLUE
        const val ITEM_COLOR = Color.WHITE
        const val FOLDER_NAME = "Google Apps"
    }

    fun isAppInitialized() : Boolean {
        Timber.d("isAppInitialized=${sharedPreferences.getBoolean(PREFS_INITIALIZED, false)}")
        return sharedPreferences.getBoolean(PREFS_INITIALIZED, false)
    }

    private fun commitInitialized() {
        sharedPreferences.edit().apply {
            putBoolean(PREFS_INITIALIZED, true)
            apply()
        }
        Timber.d("Successfully set default variables")
    }

    suspend fun initialize() = withContext(Dispatchers.Default) {
        Timber.i("Initializing first launch settings ...")
        withContext(Dispatchers.Default) { createThemes() }
        createDefaultAppsFolder(createFirstPage())
        commitInitialized()
    }

    private fun isAppInstalled(packageName: String): Boolean {
        return try {
            packageManager.getApplicationInfo(packageName, 0).enabled
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    private suspend fun createDefaultAppsFolder(page: PageModel) = withContext(Dispatchers.IO) {
        val folderId = homescreenViewModel.addFolder(
            FolderModel(
                itemColor = ITEM_COLOR,
                backgroundColor = FOLDER_COLOR,
                title = FOLDER_NAME,
                pageId = page.id
            )
        )

        val items = mutableListOf<FolderItemModel>()
        var currentPosition = -1
        DEFAULT_PACKAGES.forEach { appPackage ->
            if (isAppInstalled(appPackage)) {
                Timber.d("This app (${appPackage}) is installed, adding to folder")
                currentPosition += 1
                items.add(
                    FolderItemModel(
                        folderId = folderId,
                        packageName = appPackage,
                        position = currentPosition
                    )
                )
            }
        }

        homescreenViewModel.addItems(items)
    }

    private fun createThemes() {
        Timber.d("Creating default theme profiles")
        //https://flatuicolors.com/palette/us
        val lightTheme =
            ThemeProfileModel(
                id = null,
                drawerBackgroundColor = Color.parseColor("#dfe6e9"),
                drawerTextFillColor = Color.parseColor("#2d3436"),
                drawerSearchBackgroundColor = Color.parseColor("#0984e3"),
                drawerSearchTextColor = Color.parseColor("#636e72"),
                dockBackgroundColor = Color.parseColor("#99dfe6e9"),
                dockTextColor = Color.parseColor("#2d3436"),
                isUserProfile = false,
                name = "Light Theme"
            )

        //https://flatuicolors.com/palette/us
        val darkTheme = ThemeProfileModel(
            id = null,
            drawerBackgroundColor = Color.parseColor("#2d3436"),
            drawerTextFillColor = Color.parseColor("#dfe6e9"),
            drawerSearchBackgroundColor = Color.parseColor("#0984e3"),
            drawerSearchTextColor = Color.parseColor("#2d3436"),
            dockBackgroundColor = Color.parseColor("#99636e72"),
            dockTextColor = Color.parseColor("#b2bec3"),
            name = "Dark Theme",
            isUserProfile = false
        )

        //https://flatuicolors.com/palette/es
        val blueTheme = ThemeProfileModel(
            id = null,
            drawerBackgroundColor = Color.parseColor("#2c2c54"),
            drawerTextFillColor = Color.parseColor("#f7f1e3"),
            drawerSearchBackgroundColor = Color.parseColor("#40407a"),
            drawerSearchTextColor = Color.parseColor("#aaa69d"),
            dockBackgroundColor = Color.parseColor("#99706fd3"),
            dockTextColor = Color.parseColor("#d1ccc0"),
            name = "Blue",
            isUserProfile = false
        )

        sharedPreferences.edit().apply {
            putString(AppSettingsRepository.THEME_PROFILE_FIELD, Gson().toJson(blueTheme))
            apply()
        }

        Timber.d("Default theme profiles created, inserting them into database")
        settingsViewModel.addProfile(lightTheme, darkTheme, blueTheme)
        Timber.d("Default theme profiles have been inserted")
    }

    private suspend fun createFirstPage() : PageModel {
        Timber.d("creating first page")
        homescreenViewModel.addPage(false)
        return homescreenViewModel.getFirstPage()
    }

}