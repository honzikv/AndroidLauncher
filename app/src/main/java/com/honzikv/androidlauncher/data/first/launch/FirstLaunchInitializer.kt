package com.honzikv.androidlauncher.data.first.launch

import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import com.google.gson.Gson
import com.honzikv.androidlauncher.data.model.FolderModel
import com.honzikv.androidlauncher.data.model.FolderItemModel
import com.honzikv.androidlauncher.data.model.PageModel
import com.honzikv.androidlauncher.data.model.ThemeProfileModel
import com.honzikv.androidlauncher.data.repository.*
import com.honzikv.androidlauncher.data.repository.AppSettingsRepository.Companion.THEME_PROFILE_FIELD
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

const val FOLDER_COLOR = Color.BLUE
const val ITEM_COLOR = Color.WHITE
const val FOLDER_NAME = "Google Apps"

/**
 * Class that creates database objects for the first launch
 */
class FirstLaunchInitializer(
    private val folderDataRepository: FolderDataRepository,
    private val homescreenRepository: HomescreenRepository,
    private val packageManager: PackageManager,
    private val appThemeRepository: AppThemeRepository,
    private val sharedPreferences: SharedPreferences
) {

    fun isAppInitialized(): Boolean {
        Timber.d("initialized: %s", sharedPreferences.getBoolean(PREFS_INITIALIZED, false))
        return sharedPreferences.getBoolean(PREFS_INITIALIZED, false)
    }

    suspend fun initialize() = withContext(Dispatchers.Default) {
        Timber.i("Initializing default settings")
        createDefaultThemeProfiles()
        val pageDeferred = async { homescreenRepository.addPageAsLast() }
        val folderDeferred = async { createGoogleFolder() }
        homescreenRepository.addFolderToPage(folderDeferred.await(), pageDeferred.await())
        commitInitialized()
    }

    private fun commitInitialized() {
        val editor = sharedPreferences.edit()
        editor.putBoolean(PREFS_INITIALIZED, true)
        editor.apply()
        Timber.d("Successfully set default variables")
    }


    /**
     * Creates folder_header with google apps
     */
    private suspend fun createGoogleFolder(): Long = withContext(Dispatchers.IO) {
        Timber.d("Creating default google apps folder")
        val folderId = folderDataRepository.addFolder(
            FolderModel(
                itemColor = ITEM_COLOR,
                backgroundColor = FOLDER_COLOR,
                title = FOLDER_NAME
            )
        )

        val appList = mutableListOf<FolderItemModel>()
        DEFAULT_PACKAGES.forEach { appPackage ->
            if (isAppInstalled(appPackage)) {
                Timber.d("This app (${appPackage}) is installed, adding to folder")
                appList.add(
                    FolderItemModel(
                        folderId = folderId,
                        systemAppPackageName = appPackage
                    )
                )
            }
        }

        val folder = folderDataRepository.getFolder(folderId)

        appList.forEach { folderItem ->
            folderDataRepository.addAppToFolder(folderItem, folder)
        }

        Timber.d("Google folder created")
        return@withContext folderId
    }

    private fun isAppInstalled(packageName: String): Boolean {
        return try {
            packageManager.getApplicationInfo(packageName, 0).enabled
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    private suspend fun createDefaultThemeProfiles() = withContext(Dispatchers.IO) {
        Timber.d("Creating default theme profiles")
        //https://flatuicolors.com/palette/us
        val lightTheme =
            ThemeProfileModel(
                id = null,
                drawerBackgroundColor = Color.parseColor("#dfe6e9"),
                drawerTextFillColor = Color.parseColor("#2d3436"),
                drawerSearchBackgroundColor = Color.parseColor("#0984e3"),
                drawerSearchTextColor = Color.parseColor("#636e72"),
                dockBackgroundColor = Color.parseColor("#dfe6e9"),
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
            dockBackgroundColor = Color.parseColor("#636e72"),
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
            dockBackgroundColor = Color.parseColor("#706fd3"),
            dockTextColor = Color.parseColor("#d1ccc0"),
            name = "Blue",
            isUserProfile = false
        )

        sharedPreferences.edit().putString(THEME_PROFILE_FIELD, Gson().toJson(blueTheme))

        Timber.d("Default theme profiles created, inserting them into database")
        appThemeRepository.addProfile(lightTheme, darkTheme, blueTheme)
        Timber.d("Default theme profiles have been inserted")
    }
}
