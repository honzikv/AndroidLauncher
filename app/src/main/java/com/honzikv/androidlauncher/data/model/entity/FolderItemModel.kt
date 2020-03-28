package com.honzikv.androidlauncher.data.model.entity

import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey

/**
 * User created app shortcut - e.g icon in folder
 */
@Fts4
@Entity
data class FolderItemModel(

    @PrimaryKey(autoGenerate = true)
    val id: Int,

    /**
     * Reference to SystemApp via package name
     */
    val systemAppPackageName: String
)