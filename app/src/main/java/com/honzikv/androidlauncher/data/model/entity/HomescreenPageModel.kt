package com.honzikv.androidlauncher.data.model.entity

import androidx.room.*

@Entity
data class HomescreenPageModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,

    var pageNumber: Int = 0,

    @Ignore
    var folderList: List<FolderModel>? = null,

    var nextFolderPosition: Int = 0
)

data class HomescreenPageFolders(
    @Embedded
    val homescreenPage: HomescreenPageModel,

    @Relation(parentColumn = "id", entityColumn = "id", entity = FolderModel::class)
    val folderList: List<FolderModel>
)