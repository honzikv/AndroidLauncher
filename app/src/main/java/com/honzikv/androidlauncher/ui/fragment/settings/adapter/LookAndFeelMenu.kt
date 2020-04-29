package com.honzikv.androidlauncher.ui.fragment.settings.adapter

import android.content.Context
import android.widget.Toast
import android.widget.Toast.makeText
import com.honzikv.androidlauncher.data.model.entity.ThemeProfileModel
import com.honzikv.androidlauncher.viewmodel.SettingsViewModel
import com.multilevelview.models.RecyclerViewItem

class LookAndFeelMenu(viewModel: SettingsViewModel, context: Context) {

    var position = -1

    companion object {
        const val LOOK_AND_FEEL = "Look and Feel"
        const val LOOK_AND_FEEL_SUB = "Customize theme of your launcher"

        const val CURRENTLY_SELECTED_THEME = "Currently Selected Theme"

        const val SELECT_THEME = "Change Theme"
        const val SWIPE_DOWN_NOTIFICATIONS = "Swipe Down for Notification Panel"
        const val USE_ANIMATIONS = "Use Animations"
        const val SHOW_DOCK = "Show Dock"
        const val ONE_HANDED_MODE = "Use One Handed Mode"
    }

    private val lookAndFeel =
        HeaderItem(LOOK_AND_FEEL, LOOK_AND_FEEL_SUB, 0).apply { showChildren = true }

    val currentTheme = TextLeftRightItem(CURRENTLY_SELECTED_THEME, "", { }, lookAndFeel.level + 1)

    val selectTheme = SpinnerItem(
        SELECT_THEME,
        mutableListOf(),
        {
            makeText(context,"LOL",Toast.LENGTH_LONG).show()
            viewModel.changeTheme(it as ThemeProfileModel)
        },
        context,
        1
    )

    private val swipeDownToOpenNotificationsSwitchItem = SwitchItem(
        SWIPE_DOWN_NOTIFICATIONS,
        viewModel.getSwipeDownForNotifications(),
        { viewModel.setSwipeDownForNotifications(it) },
        1
    )

    private val showDock = SwitchItem(
        SHOW_DOCK,
        viewModel.getShowDock(),
        { viewModel.setShowDock(it) },
        1
    )

    val oneHandedMode = SwitchItem(
        ONE_HANDED_MODE,
        viewModel.getUseOneHandedMode(),
        { viewModel.setUseOneHandedMode(it) },
        1
    )

    init {
        lookAndFeel.addChildren(
            listOf(
                currentTheme,
                selectTheme,
                swipeDownToOpenNotificationsSwitchItem,
                showDock,
                oneHandedMode
            )
        )
    }

    fun getRoot() = lookAndFeel as RecyclerViewItem

}