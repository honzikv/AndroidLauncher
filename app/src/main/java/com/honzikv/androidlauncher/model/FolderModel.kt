package com.honzikv.androidlauncher.model

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
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FolderModel

        if (id != other.id) return false
        if (pageId != other.pageId) return false
        if (position != other.position) return false
        if (backgroundColor != other.backgroundColor) return false
        if (itemColor != other.itemColor) return false
        if (title != other.title) return false
        if (nextAppPosition != other.nextAppPosition) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (pageId?.hashCode() ?: 0)
        result = 31 * result + (position ?: 0)
        result = 31 * result + backgroundColor
        result = 31 * result + itemColor
        result = 31 * result + title.hashCode()
        result = 31 * result + nextAppPosition
        return result
    }

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
    val packageName: String,

    var position: Int = 0

) {

    @Ignore
    var icon: Drawable? = null

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
    fun restoreState(folderWithItems: FolderWithItems) {
        Timber.d("setting show items to ${folderWithItems.showItems}")
        showItems = folderWithItems.showItems
    }

    @Ignore
    var showItems: Boolean = false
}