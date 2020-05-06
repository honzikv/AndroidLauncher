package com.honzikv.androidlauncher.ui.fragment.settings.adapter

import android.content.Context
import android.graphics.drawable.Drawable
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
    var isChecked: Boolean,
    /**
     * Function on switch that returns if the switch is now ON or Off
     */
    private val functionOnClick: (Boolean) -> Unit,
    level: Int
) : RecyclerViewItem(level) {

    fun performClick(checked: Boolean) {
        functionOnClick(checked)
        isChecked = checked
    }
}

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
    items: List<Displayable>,
    val functionOnClick: (Displayable) -> Unit,
    val context: Context,
    level: Int
) : RecyclerViewItem(level) {

    val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, items)
}

class SubHeaderItem(
    val textLeft: String,
    level: Int
) : RecyclerViewItem(level)

/**
 * Tag interface
 */
interface HomescreenItem

class SettingsPageItem(
    val pageName: String,
    val remove: () -> Unit,
    val addFolder: () -> Unit,
    level: Int
) : RecyclerViewItem(level), HomescreenItem

class SettingsFolderItem(
    val folderName: String,
    val remove: () -> Unit,
    val addItem: () -> Unit,
    level: Int
) : RecyclerViewItem(level), HomescreenItem

class SettingAppItem(
    val packageName: String,
    val icon: Drawable,
    val remove: () -> Unit,
    level: Int
) : RecyclerViewItem(level), HomescreenItem