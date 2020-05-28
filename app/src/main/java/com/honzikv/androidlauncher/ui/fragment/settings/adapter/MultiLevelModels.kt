package com.honzikv.androidlauncher.ui.fragment.settings.adapter

import android.content.Context
import android.widget.ArrayAdapter
import com.multilevelview.models.RecyclerViewItem
import timber.log.Timber

/**
 * Tridy jednotlivych typu objektu v nastaveni
 */

/**
 * Hlavicka - nadpis "Settings"
 */
class Header(val text: String, level: Int) : RecyclerViewItem(level)

/**
 * CardView s jednotlivymi druhy nastaveni
 * [headerText] - nazev menu
 * [headerSubText] - text pod nazvem
 */
class HeaderItem(val headerText: String, val headerSubText: String, level: Int) :
    RecyclerViewItem(level)

/**
 * Switch komponenta
 * [textLeft] - text vlevo od switche
 * [isChecked] - zda-li je switch aktivni
 * [functionOnClick] - lambda funkce, ktera se vykona po zmene stavu switche
 */
class SwitchItem(
    val textLeft: String,
    var isChecked: Boolean,
    private val functionOnClick: (Boolean) -> Unit,
    level: Int
) : RecyclerViewItem(level) {

    /**
     * Provede kliknuti
     */
    fun performClick(checked: Boolean) {
        Timber.d("$textLeft -> Performing click")
        isChecked = checked
        functionOnClick(checked)
    }
}

/**
 * Text vlevo i vpravo
 * [textLeft] - text vlevo
 * [textRight] - text vpravo
 * [functionOnClick] - funkce po kliknuti
 */
class TextLeftRightItem(
    val textLeft: String,
    var textRight: String,
    val functionOnClick: () -> Unit,
    level: Int
) : RecyclerViewItem(level)

/**
 * [textLeft] - text vlevo
 * [functionOnClick] - funkce po kliknuti
 */
class TextLeftItem(
    val textLeft: String,
    val functionOnClick: () -> Unit,
    level: Int
) : RecyclerViewItem(level)

/**
 * Pro spinner jsem pouzil tagovaci interface, slo by implementovat i jinak ale tato implementace je
 * nejkratsi a nejefektivnejsi
 */
interface Displayable

/**
 * Spinner pro zobrazeni seznamu hodnot
 * [textLeft] - text vlevo od spinneru
 * [items] - predmety v seznamu hodnot
 * [functionOnClick] - funkce, ktera se provede pri kliknuti a vyberu prvku
 * [context] - aplikacni kontext
 */
class SpinnerItem(
    val textLeft: String,
    items: List<Displayable>,
    val functionOnClick: (Displayable) -> Unit,
    val context: Context,
    level: Int
) : RecyclerViewItem(level) {
    /**
     * Adapter pro zmenu dat
     */
    val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, items)
}

/**
 * CardView pro detail casti nastaveni, obsahuje ikonku pro vizualizaci zobrazeni detailu
 * [textLeft] - text vlevo
 */
class SubHeaderItem(
    val textLeft: String,
    level: Int
) : RecyclerViewItem(level)