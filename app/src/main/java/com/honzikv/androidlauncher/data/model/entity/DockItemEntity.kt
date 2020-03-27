package com.honzikv.androidlauncher.data.model.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Fts4
import androidx.room.PrimaryKey

@Fts4
@Entity
data class DockItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    val systemAppPackageName: String,

    @ForeignKey(entity = DockEntity::class)
    val dock: DockEntity

)