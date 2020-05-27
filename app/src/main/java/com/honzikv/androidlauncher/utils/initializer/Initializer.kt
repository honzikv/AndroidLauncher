package com.honzikv.androidlauncher.utils.initializer

import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Color.*
import com.honzikv.androidlauncher.model.FolderItemModel
import com.honzikv.androidlauncher.model.FolderModel
import com.honzikv.androidlauncher.model.PageModel
import com.honzikv.androidlauncher.model.ThemeProfileModel
import com.honzikv.androidlauncher.repository.AppThemeRepository.Companion.lightTheme
import com.honzikv.androidlauncher.repository.PREFS_INITIALIZED
import com.honzikv.androidlauncher.utils.applyAlpha
import com.honzikv.androidlauncher.viewmodel.HomescreenViewModel
import com.honzikv.androidlauncher.viewmodel.SettingsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * Trida, ktera inicializuje pocatecni data pri prvnim startu aplikace
 */
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

        val FOLDER_COLOR = parseColor("#fb212d40")
        val ITEM_COLOR = parseColor("#e8f1f2")
        const val FOLDER_NAME = "Google Apps"
    }

    fun isAppInitialized(): Boolean {
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
        val page = createFirstPage()
        Timber.d("$page")
        createDefaultAppsFolder(page)
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
        val folderId = homescreenViewModel.addFolderSuspend(
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

    /**
     * Vytvori temata aplikace
     */
    private suspend fun createThemes() = withContext(Dispatchers.IO) {
        Timber.d("Creating default theme profiles")

        val darkTheme = ThemeProfileModel(
            name = "Dark Theme",
            drawerBackgroundColor = parseColor("#2f3542"),
            drawerTextFillColor = parseColor("#ecf0f1"),
            drawerSearchBackgroundColor = parseColor("#34495e"),
            drawerSearchTextColor = parseColor("#bdc3c7"),
            dockTextColor = parseColor("#95a5a6"),
            dockBackgroundColor = applyAlpha(parseColor("#34495e"), 200),
            switchBackgroundColor = parseColor("#95a5a6"),
            switchThumbColorOff = parseColor("#ff6b81"),
            switchThumbColorOn = parseColor("#7bed9f")
        )

        val blueTheme = ThemeProfileModel(
            name = "Blue",
            drawerBackgroundColor = parseColor("#2c2c54"),
            drawerTextFillColor = parseColor("#f7f1e3"),
            drawerSearchBackgroundColor = parseColor("#40407a"),
            drawerSearchTextColor = parseColor("#aaa69d"),
            dockBackgroundColor = applyAlpha(parseColor("#706fd3"), 200),
            dockTextColor = parseColor("#d1ccc0"),
            switchBackgroundColor = parseColor("#57606f"),
            switchThumbColorOn = parseColor("#7bed9f"),
            switchThumbColorOff = parseColor("#a4b0be")
        )

        val amoled = ThemeProfileModel(
            name = "AMOLED",
            drawerBackgroundColor = parseColor("#000000"),
            drawerTextFillColor = parseColor("#d2dae2"),
            drawerSearchBackgroundColor = parseColor("#1e272e"),
            drawerSearchTextColor = parseColor("#34e7e4"),
            dockBackgroundColor = applyAlpha(parseColor("#1e272e"), 200),
            dockTextColor = parseColor("#d2dae2"),
            switchBackgroundColor = parseColor("#808e9b"),
            switchThumbColorOn = parseColor("#a4b0be"),
            switchThumbColorOff = parseColor("#a4b0be")
        )

        val blackwoods = ThemeProfileModel(
            name = "Black Woods",
            drawerBackgroundColor = parseColor("#0c1618"),
            drawerTextFillColor = parseColor("#faf4d3"),
            drawerSearchBackgroundColor = parseColor("#004643"),
            drawerSearchTextColor = parseColor("#faf4d3"),
            dockBackgroundColor = applyAlpha(parseColor("#f6be9a"), 200),
            dockTextColor = parseColor("#0c1618"),
            switchBackgroundColor = parseColor("#6b6254"),
            switchThumbColorOn = parseColor("#d1ac00"),
            switchThumbColorOff = parseColor("#d1ac00")
        )

        val spaceOrange = ThemeProfileModel(
            name = "Space Orange",
            drawerBackgroundColor = parseColor("#0c2461"),
            drawerTextFillColor = parseColor("#fad390"),
            drawerSearchBackgroundColor = parseColor("#e55039"),
            drawerSearchTextColor = parseColor("#f8c291"),
            dockBackgroundColor = applyAlpha(parseColor("#0a3d62"), 200),
            dockTextColor = parseColor("#b8e994"),
            switchBackgroundColor = parseColor("#60a3bc"),
            switchThumbColorOn = parseColor("#e58e26"),
            switchThumbColorOff = parseColor("#fad390")
        )

        val nuclearWaste = ThemeProfileModel(
            name = "Nuclear Waste",
            drawerBackgroundColor = parseColor("#2f243a"),
            drawerTextFillColor = parseColor("#c7f9cc"),
            drawerSearchBackgroundColor = parseColor("#57cc99"),
            drawerSearchTextColor = parseColor("#444054"),
            dockBackgroundColor = applyAlpha(parseColor("#2f243a"), 200),
            dockTextColor = parseColor("#bebbbb"),
            switchBackgroundColor = parseColor("#dfe4ea"),
            switchThumbColorOn = parseColor("#70a1ff"),
            switchThumbColorOff = parseColor("#dfe4ea")
        )

        val cleanWhite = ThemeProfileModel(
            name = "Clean White",
            drawerBackgroundColor = parseColor("#ffffff"),
            drawerTextFillColor = parseColor("#2f3542"),
            drawerSearchBackgroundColor = parseColor("#ced6e0"),
            drawerSearchTextColor = parseColor("#57606f"),
            dockBackgroundColor = applyAlpha(parseColor("#dfe4ea"), 200),
            dockTextColor = parseColor("#2f3542"),
            switchBackgroundColor = parseColor("#a4b0be"),
            switchThumbColorOn = parseColor("#ff4757"),
            switchThumbColorOff = parseColor("#2f3542")
        )

        val cocaCola = ThemeProfileModel(
            name = "Coca Cola",
            drawerBackgroundColor = parseColor("#fb212d40"),
            drawerTextFillColor = parseColor("#e5e5e5"),
            drawerSearchBackgroundColor = parseColor("#7d4e57"),
            drawerSearchTextColor = parseColor("#e5e5e5"),
            dockBackgroundColor = applyAlpha(parseColor("#364156"), 200),
            dockTextColor = parseColor("#e6ebe0"),
            switchBackgroundColor = parseColor("#747d8c"),
            switchThumbColorOn = parseColor("#5352ed"),
            switchThumbColorOff = parseColor("#a4b0be")
        )

        Timber.d("Default theme profiles created, inserting them into database")
        settingsViewModel.addProfile(
            lightTheme,
            darkTheme,
            amoled,
            cleanWhite,
            blueTheme,
            blackwoods,
            spaceOrange,
            nuclearWaste,
            cocaCola
        )
        Timber.d("Default theme profiles have been inserted")
    }

    private suspend fun createFirstPage(): PageModel {
        Timber.d("creating first page")
        homescreenViewModel.addPageSuspend()
        return homescreenViewModel.getFirstPage()
    }

}