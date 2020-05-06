package com.honzikv.androidlauncher.ui.fragment.settings.menu

import com.multilevelview.models.RecyclerViewItem
import kotlin.properties.Delegates

abstract class MultiLevelMenu {

    var position by Delegates.notNull<Int>()

    abstract fun getRoot(): RecyclerViewItem
}