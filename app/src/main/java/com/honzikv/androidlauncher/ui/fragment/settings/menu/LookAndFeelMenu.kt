package com.honzikv.androidlauncher.ui.fragment.settings.menu

import android.content.Context
import com.honzikv.androidlauncher.model.ThemeProfileModel
import com.honzikv.androidlauncher.ui.fragment.settings.adapter.HeaderItem
import com.honzikv.androidlauncher.ui.fragment.settings.adapter.SpinnerItem
import com.honzikv.androidlauncher.ui.fragment.settings.adapter.SwitchItem
import com.honzikv.androidlauncher.ui.fragment.settings.adapter.TextLeftRightItem
import com.honzikv.androidlauncher.viewmodel.SettingsViewModel

/**
 * Vytvoreni header menu pro "Look and Feel". Trida existuje pouze proto, aby nebyl fragment nastaveni
 * prilis dlouhy a nepreheldny - vytvori header spolu s potomky, ktere se zobrazi po rozkliknuti.
 */
class LookAndFeelMenu(
    /**
     * Viewmodel z fragmentu
     */
    viewModel: SettingsViewModel,
    /**
     * Aplikacni kontext
     */
    context: Context
) : MultiLevelMenu() {

    override fun getRoot() = lookAndFeel

    companion object {
        const val LOOK_AND_FEEL = "Look and Feel"
        const val LOOK_AND_FEEL_SUB = "Change App's Look and Feel"

        const val CURRENTLY_SELECTED_THEME = "Current Theme "

        const val SELECT_THEME = "Change Theme"
        const val SWIPE_DOWN_NOTIFICATIONS = "Swipe Down for Notifications"

    }

    private val lookAndFeel = HeaderItem(LOOK_AND_FEEL, LOOK_AND_FEEL_SUB, 0)

    val currentTheme = TextLeftRightItem(
        CURRENTLY_SELECTED_THEME,
        "",
        { },
        lookAndFeel.level + 1
    )

    val selectTheme = SpinnerItem(
        SELECT_THEME,
        mutableListOf(),
        { viewModel.changeTheme(it as ThemeProfileModel) },
        context,
        lookAndFeel.level + 1
    )

    private val swipeDownToOpenNotificationsSwitchItem = SwitchItem(
        SWIPE_DOWN_NOTIFICATIONS,
        viewModel.getSwipeDownForNotifications(),
        { viewModel.setSwipeDownForNotifications(it) },
        lookAndFeel.level + 1
    )

    init {
        lookAndFeel.addChildren(
            listOf(
                currentTheme, selectTheme, swipeDownToOpenNotificationsSwitchItem
            )
        )
    }


}
