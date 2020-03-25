package com.honzikv.androidlauncher.data.model

import android.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Effectively dock is a singleton
 */
@Entity
data class DockModel(

    @PrimaryKey
    val primaryKey: Int = DOCK_PRIMARY_KEY,

    var color: Int = DOCK_DEFAULT_COLOR,

    var opacity: Float = DEFAULT_OPACITY
) {
    companion object {
        const val DOCK_PRIMARY_KEY = 1

        const val DOCK_DEFAULT_COLOR = Color.WHITE

        const val DEFAULT_OPACITY = 0.8f
    }
}