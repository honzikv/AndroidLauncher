package com.honzikv.androidlauncher.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.honzikv.androidlauncher.ui.fragment.settings.adapter.Displayable

/**
 * Entita reprezentujici tema celkoveho vzhledu v aplikaci
 */
@Entity
data class ThemeProfileModel(

    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,

    /**
     * Pozadi draweru
     */
    val drawerBackgroundColor: Int,

    /**
     * Barva textu u aplikaci v draweru
     */
    val drawerTextFillColor: Int,

    /**
     * Barva vyhledavaciho pole v draweru
     */
    val drawerSearchBackgroundColor: Int,

    /**
     * Barva textu v draweru
     */
    val drawerSearchTextColor: Int,

    /**
     * Pozadi doku
     */
    val dockBackgroundColor: Int,

    /**
     * Barva textu v doku
     */
    val dockTextColor: Int,

    /**
     * Barva On stavu pro switch
     */
    val switchThumbColorOn: Int,

    /**
     * Barva Off stavu pro switch
     */
    val switchThumbColorOff: Int,

    /**
     * Pozadi switche
     */
    val switchBackgroundColor: Int,

    /**
     * Jmeno
     */
    var name: String
) :
/**
 * Tag interface pro Spinner
 */
    Displayable {

    /**
     * Override pro zobrazeni jmena ve spinneru
     */
    override fun toString() = name
}