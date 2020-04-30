package com.honzikv.androidlauncher.ui.fragment.settings.menu

import android.content.Context
import com.honzikv.androidlauncher.ui.fragment.settings.adapter.HeaderItem
import com.honzikv.androidlauncher.ui.fragment.settings.adapter.SwitchItem
import com.honzikv.androidlauncher.viewmodel.SettingsViewModel
import com.multilevelview.models.RecyclerViewItem

class DrawerMenu(viewModel: SettingsViewModel, context: Context) {

    var position = -1

    companion object {
        const val DRAWER_SETTINGS = "Drawer Settings"
        const val DRAWER_SETTINGS_SUB = "Customize Drawer"

        const val SHOW_AS_GRID = "Show Drawer as Grid"

        const val SHOW_SEARCH_BAR = "Show Search Bar"

        const val USE_ROUND_CORNERS = "Use Round Corners"
    }

    private val drawerSettings = HeaderItem(DRAWER_SETTINGS, DRAWER_SETTINGS_SUB, 0)

    private val showSearchBar = SwitchItem(
        SHOW_SEARCH_BAR,
        viewModel.getShowSearchBar(),
        { viewModel.setShowSearchBar(it) },
        1
    )

    private val showAsGrid = SwitchItem(
        SHOW_AS_GRID,
        viewModel.getShowDrawerAsGrid(),
        { viewModel.setShowDrawerAsGrid(it) },
        drawerSettings.level + 1
    )

    private val useRoundCorners = SwitchItem(
        USE_ROUND_CORNERS,
        viewModel.getUseRoundCorners(),
        { viewModel.setUseRoundCorners(it) },
        drawerSettings.level + 1
    )

    init {
        drawerSettings.addChildren(
            listOf(
                showSearchBar,
                showAsGrid,
                useRoundCorners
            )
        )
    }

    fun getRoot() = drawerSettings as RecyclerViewItem
}