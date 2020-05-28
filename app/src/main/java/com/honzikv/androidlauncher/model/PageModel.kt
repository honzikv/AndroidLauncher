package com.honzikv.androidlauncher.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entita reprezentuje stranku na plose
 */
@Entity
data class PageModel(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,

    /**
     * Cislo stranky
     */
    var pageNumber: Int = 0,

    /**
     * Dalsi pozice slozky
     */
    var nextFolderPosition: Int = 0
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PageModel

        if (id != other.id) return false
        if (pageNumber != other.pageNumber) return false
        if (nextFolderPosition != other.nextFolderPosition) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + pageNumber
        result = 31 * result + nextFolderPosition
        return result
    }
}
