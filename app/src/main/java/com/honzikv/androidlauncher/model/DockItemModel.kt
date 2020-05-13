package com.honzikv.androidlauncher.model

import android.graphics.drawable.Drawable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class DockItemModel(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,

    val packageName: String,

    var position: Int = 0

) {
    @Ignore
    var icon: Drawable? = null

    @Ignore
    var label: String? = null
}