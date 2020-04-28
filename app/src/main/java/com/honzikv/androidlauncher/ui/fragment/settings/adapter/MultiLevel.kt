package com.honzikv.androidlauncher.ui.fragment.settings.adapter

import android.content.Context
import android.view.View
import android.widget.ArrayAdapter
import androidx.lifecycle.LiveData
import com.multilevelview.models.RecyclerViewItem
import kotlin.reflect.KFunction

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

class SwitchItem(
    val textLeft: String,
    val isChecked: Boolean,
    val functionOnClick: (Boolean) -> Unit,
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

class SpinnerItem<T>(
    val textLeft: String,
    var items: List<T>,
    val functionOnClick: (T) -> Unit,
    val context: Context,
    level: Int
) : RecyclerViewItem(level) {

    val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, items)
}