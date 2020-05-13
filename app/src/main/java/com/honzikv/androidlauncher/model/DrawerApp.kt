package com.honzikv.androidlauncher.model

import android.graphics.drawable.Drawable

data class DrawerApp(
    val packageName: String,
    val label: String,
    val icon: Drawable
) {
    override fun toString(): String {
        return "DrawerApp(packageName='$packageName', label='$label', icon=$icon)"
    }
}
