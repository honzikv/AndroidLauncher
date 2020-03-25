package com.honzikv.androidlauncher.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FolderModel(
    @PrimaryKey(autoGenerate = true)
    val primaryKey: Int = 1,

    val page: Int,

    val position: Int,

    val backgroundColor: Int,

    val title: String
)