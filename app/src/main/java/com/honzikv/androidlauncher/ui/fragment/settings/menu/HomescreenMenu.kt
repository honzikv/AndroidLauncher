package com.honzikv.androidlauncher.ui.fragment.settings.menu

import android.content.Intent
import androidx.fragment.app.FragmentActivity
import com.honzikv.androidlauncher.ui.fragment.settings.adapter.HeaderItem
import com.honzikv.androidlauncher.ui.fragment.settings.adapter.TextLeftItem
import com.honzikv.androidlauncher.viewmodel.SettingsViewModel
import com.multilevelview.models.RecyclerViewItem

class HomescreenMenu(viewModel: SettingsViewModel, fragmentActivity: FragmentActivity) :
    MultiLevelMenu() {

    companion object {
        const val HOMESCREEN_SETTINGS = "Homescreen"
        const val HOMESCREEN_SETTINGS_SUB = "Configure Pages and Folders"

        const val CHANGE_WALLPAPER = "Change Wallpaper"
    }

    private val homescreenMenu = HeaderItem(HOMESCREEN_SETTINGS, HOMESCREEN_SETTINGS_SUB, 0)

    override fun getRoot() = homescreenMenu

    private val changeWallpaper = TextLeftItem(
        CHANGE_WALLPAPER, {
            fragmentActivity.startActivity(Intent(Intent.ACTION_SET_WALLPAPER))
        },
        homescreenMenu.level + 1
    )

    init {
        homescreenMenu.addChildren(
            listOf(
                changeWallpaper
            )
        )
    }
}