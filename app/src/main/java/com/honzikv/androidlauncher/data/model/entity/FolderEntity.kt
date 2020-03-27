package com.honzikv.androidlauncher.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FolderEntity(
    @PrimaryKey(autoGenerate = true)
    val primaryKey: Int = 1,

    val page: Int,

    val position: Int,

    val backgroundColor: Int,

    val title: String
)