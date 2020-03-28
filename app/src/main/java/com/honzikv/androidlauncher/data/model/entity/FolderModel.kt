package com.honzikv.androidlauncher.data.model.entity

import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey

@Entity
@Fts4
data class FolderModel(
    @PrimaryKey(autoGenerate = true)
    val primaryKey: Int = 1,

    var page: Int,

    var position: Int,

    var backgroundColor: Int,

    var title: String,

    var appList: List<FolderItemModel>
)