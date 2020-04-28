package com.honzikv.androidlauncher.data.model.entity

import android.graphics.drawable.Drawable
import androidx.room.*

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = PageDto::class,
            onDelete = ForeignKey.CASCADE,
            parentColumns = ["id"],
            childColumns = ["pageId"]
        )]
)
data class FolderDto(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,

    var pageId: Long? = null,

    var position: Int? = null,

    var backgroundColor: Int,

    var itemColor: Int,

    var title: String,

    var nextAppPosition: Int = 0
)

/**
 * User created app shortcut - e.g icon in folder_header
 */
@Entity(
    foreignKeys = [
        ForeignKey(
            entity = FolderDto::class,
            onDelete = ForeignKey.CASCADE,
            parentColumns = ["id"],
            childColumns = ["folderId"]
        )]
)
data class FolderItemDto(

    @PrimaryKey(autoGenerate = true)
    var id: Long? = 0,

    var folderId: Long,

    /**
     * Reference to SystemApp via package name
     */
    val systemAppPackageName: String,

    var position: Int? = null

) {

    @Ignore
    var drawable: Drawable? = null

    @Ignore
    var label: String? = null
}


data class PageWithFolders(
    @Embedded
    val page: PageDto,

    /**
     * List of Folders each containing list of their items
     */
    @Relation(parentColumn = "id", entityColumn = "pageId", entity = FolderDto::class)
    val folderList: List<FolderWithItems>
)

data class FolderWithItems(
    @Embedded
    val folder: FolderDto,

    /**
     * List of items in folder_header
     */
    @Relation(parentColumn = "id", entityColumn = "folderId", entity = FolderItemDto::class)
    val itemList: List<FolderItemDto>
) {
    @Ignore
    var showItems: Boolean = false
}