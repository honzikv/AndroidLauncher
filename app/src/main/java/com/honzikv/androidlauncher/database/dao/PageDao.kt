package com.honzikv.androidlauncher.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.honzikv.androidlauncher.model.FolderItemModel
import com.honzikv.androidlauncher.model.PageModel
import com.honzikv.androidlauncher.model.PageWithFolders

/**
 * Dao pro manipulaci se strankou a jejimi slozkami
 */
@Dao
interface PageDao {

    /**
     * Nacte vsechny
     */
    @Transaction
    @Query("SELECT * FROM PageModel")
    fun getAllPagesWithFoldersLiveData(): LiveData<List<PageWithFolders>>

    @Update
    suspend fun updatePage(page: PageModel)

    @Delete
    suspend fun deletePage(page: PageModel)

    @Query("SELECT * FROM PageModel ORDER BY pageNumber ASC")
    suspend fun getAllPages(): MutableList<PageModel>

    @Insert
    suspend fun addPage(page: PageModel): Long

    @Update
    suspend fun updatePageList(pages: List<PageModel>)

    @Query("SELECT * FROM PageModel WHERE id = :pageId")
    suspend fun getPage(pageId: Long): PageModel

    @Query("SELECT MAX(pageNumber) FROM PageModel")
    suspend fun getLastPageNumber(): Int?

    @Transaction
    @Query("SELECT * FROM PageModel WHERE id = :pageId")
    fun getPageWithFoldersLiveData(pageId: Long): LiveData<PageWithFolders>

    @Transaction
    @Query("SELECT * FROM PageModel WHERE id = :pageId")
    suspend fun getPageWithFolders(pageId: Long): PageWithFolders

    @Query("SELECT * FROM PageModel WHERE pageNumber = 0")
    suspend fun getFirstPage(): PageModel
}