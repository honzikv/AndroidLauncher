package com.honzikv.androidlauncher.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.honzikv.androidlauncher.data.model.entity.PageDto
import com.honzikv.androidlauncher.data.model.entity.PageWithFolders

@Dao
interface PageDao {

    @Transaction
    @Query("SELECT * FROM PageDto ORDER BY pageNumber ASC")
    fun getAllPages(): LiveData<List<PageWithFolders>>

    @Update
    suspend fun updatePage(page: PageDto)

    @Delete
    suspend fun deletePage(page: PageDto)

    @Query("SELECT * FROM PageDto ORDER BY pageNumber ASC")
    suspend fun getAllPagesAsMutable() : MutableList<PageDto>

    @Insert
    suspend fun addPage(page: PageDto): Long

    @Update
    suspend fun updatePageList(pages: List<PageDto>)

    @Query("SELECT * FROM PageDto WHERE id = :pageId")
    suspend fun getPage(pageId: Long): PageDto

}