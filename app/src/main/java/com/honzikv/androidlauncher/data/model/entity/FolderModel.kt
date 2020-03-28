package com.honzikv.androidlauncher.data.model.entity

import androidx.room.*

@Entity
data class FolderModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 1,

    var page: Int,

    var position: Int,

    var backgroundColor: Int,

    var title: String,

    var appList: List<FolderItemModel>
)

@Entity
data class DockItemModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 1,

    val systemAppPackageName: String,

    var position: Int = 0

)

data class FolderItemDetails(
    @Embedded
    val folder: FolderModel,

    @Relation(parentColumn = "id", entityColumn = "id", entity = DockItemModel::class)
    val itemList: List<DockItemModel>
)