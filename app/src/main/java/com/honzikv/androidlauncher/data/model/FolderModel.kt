package com.honzikv.androidlauncher.data.model

import android.graphics.drawable.Drawable
import androidx.room.*
import timber.log.Timber

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = PageModel::class,
            onDelete = ForeignKey.CASCADE,
            parentColumns = ["id"],
            childColumns = ["pageId"]
        )]
)
data class FolderModel(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,

    var pageId: Long? = null,

    var position: Int? = null,

    var backgroundColor: Int,

    var itemColor: Int,

    var title: String,

    var nextAppPosition: Int = 0
) {
    override fun toString() = title
}

/**
 * User created app shortcut - e.g icon in folder_header
 */
@Entity(
    foreignKeys = [
        ForeignKey(
            entity = FolderModel::class,
            onDelete = ForeignKey.CASCADE,
            parentColumns = ["id"],
            childColumns = ["folderId"]
        )]
)
data class FolderItemModel(

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,

    var folderId: Long? = null,

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
    val page: PageModel,

    /**
     * List of folders each containing list of their items
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
) {
    init {
        Timber.d("Folder with items, itemlist size = ${itemList.size}")
    }

    @Ignore
    var showItems: Boolean = false
}