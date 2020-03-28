package com.honzikv.androidlauncher.data.model

import android.graphics.drawable.Drawable
import com.honzikv.androidlauncher.data.model.entity.FolderModel

data class LauncherAppModel(
    val packageName: String,

    val label: String,

    val icon: Drawable
)
