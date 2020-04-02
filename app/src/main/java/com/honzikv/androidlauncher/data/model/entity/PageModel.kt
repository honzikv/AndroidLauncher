package com.honzikv.androidlauncher.data.model.entity

import androidx.room.*

@Entity
data class PageModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,

    var pageNumber: Int = 0,

    var nextFolderPosition: Int = 0
)
