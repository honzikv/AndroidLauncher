package com.honzikv.androidlauncher.data.model

import android.graphics.drawable.Drawable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey

@Entity
@Fts4
data class SystemAppModel(

    /**
     * Package name is always unique for every app so it can be used as PK
     */
    @PrimaryKey
    val packageName: String,

    var icon: Drawable,

    var appName: String
)