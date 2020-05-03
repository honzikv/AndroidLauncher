package com.honzikv.androidlauncher.data.model

import androidx.room.*

@Entity
data class PageModel(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,

    var pageNumber: Int = 0,

    var nextFolderPosition: Int = 0
)
