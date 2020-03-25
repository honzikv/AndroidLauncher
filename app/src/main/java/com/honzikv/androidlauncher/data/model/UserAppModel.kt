package com.honzikv.androidlauncher.data.model

import android.graphics.drawable.Drawable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey

/**
 * User created app shortcut - e.g icon in folder
 */
@Fts4
@Entity
data class UserAppModel(

    /**
     * Primary key cannot be package name since more shortcuts might reference single system app
     */
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    var icon: Drawable,

    /**
     * Reference to SystemApp
     */
    val systemApp: SystemAppModel?,

    @Embedded
    var folder: FolderModel?,

    var dockPosition: Int?,

    var appName: String
)