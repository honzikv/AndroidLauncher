package com.honzikv.androidlauncher.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.honzikv.androidlauncher.model.FolderItemModel
import com.honzikv.androidlauncher.model.PageModel
import com.honzikv.androidlauncher.model.PageWithFolders

@Dao
interface PageDao {

    @Transaction
    @Query("SELECT * FROM PageModel")
    fun getAllPagesWithFoldersLiveData(): LiveData<List<PageWithFolders>>

    @Update
    suspend fun updatePage(page: PageModel)

    @Delete
    suspend fun deletePage(page: PageModel)

    @Transaction
    suspend fun deletePageAndReadjustPositions(pageId: Long) {
    }

    @Query("SELECT * FROM PageModel ORDER BY pageNumber ASC")
    suspend fun getAllPages(): MutableList<PageModel>

    @Insert
    suspend fun addPage(page: PageModel): Long

    @Update
    suspend fun updatePageList(pages: List<PageModel>)

    @Query("SELECT * FROM PageModel WHERE id = :pageId")
    suspend fun getPage(pageId: Long): PageModel

    @Query("SELECT * FROM FolderItemModel")
    fun getAllItems(): LiveData<List<FolderItemModel>>

    @Query("SELECT MAX(pageNumber) FROM PageModel")
    suspend fun getLastPageNumber(): Int?

    @Transaction
    @Query("SELECT * FROM PageModel WHERE id = :pageId")
    fun getPageWithFoldersLiveData(pageId: Long): LiveData<PageWithFolders>

    @Transaction
    @Query("SELECT * FROM PageModel WHERE id = :pageId")
    suspend fun getPageWithFolders(pageId: Long): PageWithFolders

    @Query("DELETE FROM PageModel WHERE id = :pageId")
    suspend fun deletePageWithId(pageId: Long)

    @Query("SELECT * FROM PageModel WHERE pageNumber = 0")
    suspend fun getFirstPage(): PageModel

    @Query("SELECT * FROM PageModel")
    fun getAllPagesLiveData(): LiveData<List<PageModel>>
}