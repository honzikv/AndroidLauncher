package com.honzikv.androidlauncher.ui.fragment.settings.menu

import android.content.Intent
import androidx.fragment.app.FragmentActivity
import com.honzikv.androidlauncher.ui.fragment.dock.EditDockItemsDialogFragment
import com.honzikv.androidlauncher.ui.fragment.page.EditPageListDialogFragment
import com.honzikv.androidlauncher.ui.fragment.settings.adapter.HeaderItem
import com.honzikv.androidlauncher.ui.fragment.settings.adapter.SubHeaderItem
import com.honzikv.androidlauncher.ui.fragment.settings.adapter.SwitchItem
import com.honzikv.androidlauncher.ui.fragment.settings.adapter.TextLeftItem
import com.honzikv.androidlauncher.viewmodel.SettingsViewModel
import timber.log.Timber

/**
 * Header menu pro nastaveni domovske obrazovky. Trida existuje pouze proto, aby nebyl fragment nastaveni
 * prilis dlouhy a nepreheldny - vytvori header spolu s potomky, ktere se zobrazi po rozkliknuti.
 */
class HomescreenMenu(viewModel: SettingsViewModel, fragmentActivity: FragmentActivity) :
    MultiLevelMenu() {

    companion object {
        const val HOMESCREEN_SETTINGS = "Homescreen"
        const val HOMESCREEN_SETTINGS_SUB = "Configure Pages and Folders"

        const val CHANGE_WALLPAPER = "Change Wallpaper"

        const val DOCK_SETTINGS = "Dock Settings"

        const val SHOW_DOCK = "Show Dock"

        const val MANAGE_DOCK_ITEMS = "Manage Items"

        const val SHOW_DOCK_LABELS = "Show Icon Labels"

        const val PAGE_SETTINGS = "Page Settings"

        const val MANAGE_PAGES = "Manage Pages"

        const val SHOW_PAGE_DOTS = "Show Page Dots"
    }

    private val homescreenMenu = HeaderItem(HOMESCREEN_SETTINGS, HOMESCREEN_SETTINGS_SUB, 0)

    override fun getRoot() = homescreenMenu

    /**
     * Zmena pozadi
     */
    private val changeWallpaper = TextLeftItem(
        CHANGE_WALLPAPER, {
            fragmentActivity.startActivity(Intent(Intent.ACTION_SET_WALLPAPER))
        },
        homescreenMenu.level + 1
    )

    /**
     * Subheader s nastavenim stranky
     */
    private val pageSettings = SubHeaderItem(
        PAGE_SETTINGS,
        homescreenMenu.level + 1
    )

    /**
     * Subheader s nastavenim doku
     */
    private val dockSettings = SubHeaderItem(
        DOCK_SETTINGS,
        homescreenMenu.level + 1
    )

    /**
     * Spusteni dialogu s nastavenim aplikaci v doku
     */
    private val manageDockItems = TextLeftItem(MANAGE_DOCK_ITEMS, {
        EditDockItemsDialogFragment.newInstance()
            .show(fragmentActivity.supportFragmentManager, "editDockItems")
    }, dockSettings.level + 1)

    /**
     * Zobrazeni doku
     */
    private val showDock = SwitchItem(
        SHOW_DOCK,
        viewModel.getShowDock(),
        {
            Timber.d("callback setting showDock")
            viewModel.setShowDock(it)
        },
        dockSettings.level + 1
    )

    /**
     * Zobrazeni popisku u ikon v doku
     */
    private val showDockLabels = SwitchItem(
        SHOW_DOCK_LABELS,
        viewModel.getShowDockLabels(),
        {
            Timber.d("callback setting showLabels")
            viewModel.setShowDockLabels(it)
        },
        dockSettings.level + 1
    )

    /**
     * Spusteni dialogu pro upravu stranek
     */
    private val managePages = TextLeftItem(MANAGE_PAGES, {
        EditPageListDialogFragment.newInstance()
            .show(fragmentActivity.supportFragmentManager, "editPageListFragment")
    }, pageSettings.level + 1)

    /**
     * Zobrazeni tecek nad strankami
     */
    private val showPageDots = SwitchItem(
        SHOW_PAGE_DOTS,
        viewModel.getShowPageDots(),
        { viewModel.setShowPageDots(it) },
        pageSettings.level + 1
    )

    init {
        homescreenMenu.addChildren(listOf(changeWallpaper, pageSettings, dockSettings))
        pageSettings.addChildren(listOf(managePages, showPageDots))
        dockSettings.addChildren(listOf(manageDockItems, showDockLabels, showDock))
    }


}