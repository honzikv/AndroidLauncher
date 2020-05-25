package com.honzikv.androidlauncher.model

import android.graphics.drawable.Drawable

/**
 * Entita zastupujici aplikaci v Draweru. Jedna se o namapovane hodnoty ziskane ze seznamu vsech
 * aplikaci nainstalovanych na zarizeni
 */
data class DrawerApp(
    val packageName: String,
    val label: String,
    val icon: Drawable
) {

    /**
     * Override pro debug
     */
    override fun toString(): String {
        return "DrawerApp(packageName='$packageName', label='$label', icon=$icon)"
    }
}
