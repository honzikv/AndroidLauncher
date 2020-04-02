package com.honzikv.androidlauncher.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.honzikv.androidlauncher.data.model.entity.PageModel

@Dao
interface PageDao {

    @Update
    suspend fun updatePage(page: PageModel)

    @Delete
    suspend fun deletePage(page: PageModel)

    @Query("SELECT * FROM PageModel ORDER BY pageNumber ASC")
    fun getAllPages(): LiveData<List<PageModel>>

    @Query("SELECT * FROM PageModel ORDER BY pageNumber ASC")
    suspend fun getAllPagesAsMutable() : MutableList<PageModel>

    @Insert
    suspend fun addPage(page: PageModel): Int

    @Update
    suspend fun updatePageList(pages: List<PageModel>)

    @Query("SELECT * FROM PageModel WHERE id = :pageId")
    suspend fun getPage(pageId: Int): PageModel

}