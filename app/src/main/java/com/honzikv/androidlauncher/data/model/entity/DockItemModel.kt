package com.honzikv.androidlauncher.data.model.entity

import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class DockItemModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 1,

    val packageName: String,

    var position: Int = 0

) {
    @Ignore
    var drawable: Drawable? = null
}