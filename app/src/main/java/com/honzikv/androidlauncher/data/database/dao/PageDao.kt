package com.honzikv.androidlauncher.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.honzikv.androidlauncher.data.model.FolderItemModel
import com.honzikv.androidlauncher.data.model.PageModel
import com.honzikv.androidlauncher.data.model.PageWithFolders

@Dao
interface PageDao {

    @Transaction
    @Query("SELECT * FROM PageModel ORDER BY pageNumber ASC")
    fun getAllPages(): LiveData<List<PageWithFolders>>

    @Update
    suspend fun updatePage(page: PageModel)

    @Delete
    suspend fun deletePage(page: PageModel)

    @Query("SELECT * FROM PageModel ORDER BY pageNumber ASC")
    suspend fun getAllPagesAsMutable() : MutableList<PageModel>

    @Insert
    suspend fun addPage(page: PageModel): Long

    @Update
    suspend fun updatePageList(pages: List<PageModel>)

    @Query("SELECT * FROM PageModel WHERE id = :pageId")
    suspend fun getPage(pageId: Long): PageModel

    @Query("SELECT * FROM FolderItemModel")
    fun getAllItems(): LiveData<List<FolderItemModel>>

}