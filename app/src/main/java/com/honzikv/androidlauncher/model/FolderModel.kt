package com.honzikv.androidlauncher.model

import android.graphics.drawable.Drawable
import androidx.room.*
import timber.log.Timber

/**
 * Entita reprezentujici slozku
 */
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
 * Entita reprezentujici aplikaci ve slozce
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

    /**
     * Reference na slozku
     */
    var folderId: Long? = null,

    val packageName: String,

    var position: Int = 0

) {

    @Ignore
    var icon: Drawable? = null

    @Ignore
    var label: String? = null
}

/**
 * Trida, ktera slouzi jako query stranky, pro kterou zaroven potrebujeme jeji slozky
 * a predmety ve slozkach
 */
data class PageWithFolders(
    @Embedded
    val page: PageModel,

    /**
     * Seznam vsech [FolderModel] s jejich [FolderItemModel]
     */
    @Relation(parentColumn = "id", entityColumn = "pageId", entity = FolderModel::class)
    val folderList: List<FolderWithItems>
)

/**
 * Trida, ktera slouzi jako query slozky, pro kterou zaroven potrebujeme jeji predmety
 */
data class FolderWithItems(
    @Embedded
    val folder: FolderModel,

    @Relation(parentColumn = "id", entityColumn = "folderId", entity = FolderItemModel::class)
    val itemList: List<FolderItemModel>
) {

    @Ignore
    var showItems: Boolean = false
}