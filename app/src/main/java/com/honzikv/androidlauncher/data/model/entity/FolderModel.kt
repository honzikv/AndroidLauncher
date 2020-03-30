package com.honzikv.androidlauncher.data.model.entity

import androidx.room.*

@Entity
data class FolderModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    @ForeignKey(
        entity = HomescreenPageModel::class,
        onDelete = ForeignKey.CASCADE,
        parentColumns = ["id"],
        childColumns = ["pageId"]
    )
    var pageId: Int? = null,

    var position: Int? = null,

    var backgroundColor: Int,

    var title: String,

    var nextAppPosition: Int = 0

)

/**
 * User created app shortcut - e.g icon in folder
 */
@Entity
data class FolderItemModel(

    @PrimaryKey(autoGenerate = true)
    var id: Int?,

    @ForeignKey(
        entity = FolderModel::class,
        onDelete = ForeignKey.CASCADE,
        parentColumns = ["id"],
        childColumns = ["folderId"]
    )
    var folderId: Int,

    /**
     * Reference to SystemApp via package name
     */
    val systemAppPackageName: String,

    var position: Int? = null
) {
}

data class FolderItemDetails(
    @Embedded
    val folder: FolderModel,

    @Relation(parentColumn = "id", entityColumn = "id", entity = FolderItemModel::class)
    val itemList: List<DockItemModel>
)