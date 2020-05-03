package com.honzikv.androidlauncher.ui.fragment.settings.menu

import android.content.Context
import com.honzikv.androidlauncher.ui.fragment.settings.adapter.HeaderItem
import com.honzikv.androidlauncher.ui.fragment.settings.adapter.SwitchItem
import com.honzikv.androidlauncher.viewmodel.SettingsViewModel

class DockMenu(viewModel: SettingsViewModel, context: Context) {

    companion object {
        const val DOCK_SETTINGS = "Dock Settings"
        const val DOCK_SETTINGS_SUB = "Customize Dock Options"

        const val SHOW_DOCK = "Show Dock"

        const val SHOW_LABELS = "Show Labels"

        const val NUMBER_OF_ITEMS = "Number of Items"
    }

    private val dockMenu = HeaderItem(DOCK_SETTINGS, DOCK_SETTINGS_SUB, 0)

    private val showDock =
        SwitchItem(
            SHOW_DOCK,
            viewModel.getShowDock(),
            { viewModel.setShowDock(it) },
            dockMenu.level + 1
        )

    private val showLabels = SwitchItem(
        SHOW_LABELS,
        viewModel.getShowDockLabels(),
        { viewModel.setShowDockLabels(it) },
        dockMenu.level + 1
    )


}