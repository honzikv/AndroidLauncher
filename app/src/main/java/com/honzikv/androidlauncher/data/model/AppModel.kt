package com.honzikv.androidlauncher.data.model

import android.graphics.drawable.Drawable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey

@Entity
/**
 * For quick access while searching
 */
@Fts4
data class AppModel(

    /**
     * Package name is always unique for every app so it can be used as PK
     */
    @PrimaryKey
    val packageName: String,

    var icon: Drawable,

    var appName: String,

    @Embedded
    val folder: FolderModel?,

    /**
     * Position within dock, -1 for not present in dock
     */
    val dockPosition: Int = -1
)