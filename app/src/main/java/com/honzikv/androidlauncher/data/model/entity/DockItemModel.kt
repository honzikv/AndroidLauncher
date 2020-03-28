package com.honzikv.androidlauncher.data.model.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Fts4
import androidx.room.PrimaryKey

@Fts4
@Entity
data class DockItemModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 1,

    val systemAppPackageName: String

)