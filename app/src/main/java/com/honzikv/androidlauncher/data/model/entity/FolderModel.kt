package com.honzikv.androidlauncher.data.model.entity

import androidx.room.*

@Entity
data class FolderModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    @ForeignKey(
        entity = PageModel::class,
        onDelete = ForeignKey.CASCADE,
        parentColumns = ["id"],
        childColumns = ["pageId"]
    )
    var pageId: Int? = null,

    var position: Int? = null,

    var backgroundColor: Int,

    var title: String,

    var nextAppPosition: Int = 0
) {
    var itemList: MutableList<FolderItemModel>? = null
}

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
)


data class PageFolderList(
    @Embedded
    val page: PageModel,

    /**
     * List of Folders each containing list of their items
     */
    @Relation(parentColumn = "id", entityColumn = "pageId", entity = FolderModel::class)
    val folderList: List<FolderWithItems>
)

data class FolderWithItems(
    @Embedded
    val folder: FolderModel,

    /**
     * List of items in folder
     */
    @Relation(parentColumn = "id", entityColumn = "folderId", entity = FolderItemModel::class)
    val itemList: List<FolderItemModel>
)