package com.honzikv.androidlauncher.model

import android.graphics.drawable.Drawable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

/**
 * Entita zastupujici predmet v doku
 */
@Entity
data class DockItemModel(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,

    val packageName: String,

    /**
     * Pozice v doku
     */
    var position: Int = 0

) {
    /**
     * Ikona
     */
    @Ignore
    var icon: Drawable? = null

    /**
     * Popisek
     */
    @Ignore
    var label: String? = null
}