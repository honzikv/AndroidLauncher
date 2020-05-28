package com.honzikv.androidlauncher.ui.fragment.settings.menu

import com.multilevelview.models.RecyclerViewItem
import kotlin.properties.Delegates

/**
 * Pro lepsi pouzivani jednotlivych menu v SettingsFragment
 */
abstract class MultiLevelMenu {

    /**
     * Pozice v doku
     */
    var position by Delegates.notNull<Int>()

    /**
     * Ziskani root itemu - nejvyssiho v hierarchii
     */
    abstract fun getRoot(): RecyclerViewItem
}