package com.honzikv.androidlauncher.data.model.entity

import androidx.room.*

@Entity
data class PageDto(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,

    var pageNumber: Int = 0,

    var nextFolderPosition: Int = 0
)
