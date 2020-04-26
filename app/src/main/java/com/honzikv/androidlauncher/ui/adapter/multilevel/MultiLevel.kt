package com.honzikv.androidlauncher.ui.adapter.multilevel

import android.view.View
import com.multilevelview.models.RecyclerViewItem

/**
 * Each header item only shows / hides its children
 */
class HeaderItem(
    val headerText: String,
    val headerSubText: String,
    level: Int
) : RecyclerViewItem(level) {
    var showChildren = true
}

class RadioButtonItem(
    val textLeft: String,
    val isChecked: Boolean,
    val functionOnClick: Function<Unit>,
    level: Int
) : RecyclerViewItem(level)

class TextLeftRightItem(
    val textLeft: String,
    val textRight: String,
    val functionOnClick: Function<Unit>,
    level: Int
) : RecyclerViewItem(level)

class TextLeftItem(
    val textLeft: String,
    val functionOnClick: Function<Unit>,
    level: Int
) : RecyclerViewItem(level)
