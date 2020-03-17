package com.honzikv.androidlauncher.data

import android.content.Intent
import android.graphics.drawable.Drawable

/**
 * Enum class for the type of item
 */
enum class ItemType {
    FOLDER,
    APP,
    SHORTCUT
}

/**
 * Represents any item in launcher - App Icon, Folder, Shortcut ...
 */
class LauncherItem {

    /**
     * Intent launcher via onClick method
     */
    private lateinit var intent: Intent

    /**
     * Label below the icon
     */
    private lateinit var iconLabel: String

    /**
     * Icon image
     */
    private lateinit var iconDrawable: Drawable

    /**
     * Name of package
     */
    private lateinit var packageName: String

    /**
     * Type of item
     */
    private lateinit var itemType: ItemType
}