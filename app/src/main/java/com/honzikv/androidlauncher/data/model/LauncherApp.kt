package com.honzikv.androidlauncher.data.model

import android.graphics.drawable.Drawable
import com.honzikv.androidlauncher.data.model.entity.FolderEntity

data class LauncherApp(
    val packageName: String,

    val icon: Drawable,

    val label: String,

    val appType: AppType = AppType.DRAWER,

    val folderPosition: Int = -1,

    val folder: FolderEntity? = null,

    val dockPosition: Int = -1
)