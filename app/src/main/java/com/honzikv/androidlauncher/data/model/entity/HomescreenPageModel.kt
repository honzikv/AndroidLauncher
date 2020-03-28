package com.honzikv.androidlauncher.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HomescreenPageModel(
    @PrimaryKey(autoGenerate = true)
    val pageId: Int = 1,

    val folderList: MutableList<FolderModel> = mutableListOf()
    )