package com.honzikv.androidlauncher.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.honzikv.androidlauncher.data.model.FolderItemModel
import com.honzikv.androidlauncher.data.model.PageModel
import com.honzikv.androidlauncher.data.model.PageWithFolders

@Dao
interface PageDao {

    @Transaction
    @Query("SELECT * FROM PageModel")
    fun getAllPagesLiveData(): LiveData<List<PageWithFolders>>

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

    @Query("SELECT * FROM FolderItemModel")
    fun getAllItems(): LiveData<List<FolderItemModel>>

    @Query("SELECT MAX(pageNumber) FROM PageModel")
    fun getLastPageNumber(): Int?

    @Query("SELECT * FROM PageModel WHERE id = :pageId")
    fun getPageWithFolders(pageId: Long): LiveData<PageWithFolders>

    @Query("DELETE FROM PageModel WHERE id = :pageId")
    suspend fun deletePageWithId(pageId: Long)

}