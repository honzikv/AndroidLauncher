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

class Header(val text: String, level: Int) : RecyclerViewItem(level)

class HeaderItem(
    val headerText: String,
    val headerSubText: String,
    level: Int
) : RecyclerViewItem(level)

class SwitchItem(
    val textLeft: String,
    val isChecked: Boolean,
    val functionOnClick: (Boolean) -> Unit,
    level: Int
) : RecyclerViewItem(level)

class TextLeftRightItem(
    val textLeft: String,
    var textRight: String,
    val functionOnClick: () -> Unit,
    level: Int
) : RecyclerViewItem(level)

class TextLeftItem(
    val textLeft: String,
    val functionOnClick: () -> Unit,
    level: Int
) : RecyclerViewItem(level)

/**
 * Tag interface for spinner
 */
interface Displayable {
}

/**
 * Spinner item for string list spinner
 */
class SpinnerItem(
    val textLeft: String,
    var items: List<Displayable>,
    val functionOnClick: (Displayable) -> Unit,
    val context: Context,
    level: Int
) : RecyclerViewItem(level) {

    val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, items)
}