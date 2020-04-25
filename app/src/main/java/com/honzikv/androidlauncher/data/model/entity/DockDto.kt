package com.honzikv.androidlauncher.data.model.entity

import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

const val DOCK_PRIMARY_KEY = 1

const val DOCK_DEFAULT_COLOR = Color.WHITE

const val DEFAULT_OPACITY = 0.8f

const val DOCK_APP_LIMIT = 4;

/**
 * Effectively dock is a singleton
 */
@Entity
class DockDto(

    @PrimaryKey
    val primaryKey: Int = DOCK_PRIMARY_KEY,

    var color: Int = DOCK_DEFAULT_COLOR,

    var opacity: Float = DEFAULT_OPACITY
)

@Entity
data class DockItemDto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 1,

    val packageName: String,

    var position: Int = 0

) {
    @Ignore
    var drawable: Drawable? = null
}