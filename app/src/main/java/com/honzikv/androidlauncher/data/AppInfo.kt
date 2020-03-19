package com.honzikv.androidlauncher.data

import android.graphics.drawable.Drawable
import androidx.annotation.NonNull

data class AppInfo(

    /**
     * Text of icon
     */
    val iconLabel: String,

    /**
     * Drawable icon
     */
    val iconDrawable: Drawable,

    /**
     * Name of package
     */
    val packageName: String
    )
