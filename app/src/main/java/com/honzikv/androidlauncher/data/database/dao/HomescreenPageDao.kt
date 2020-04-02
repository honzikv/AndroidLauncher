package com.honzikv.androidlauncher.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.honzikv.androidlauncher.data.model.entity.HomescreenPageFolders
import com.honzikv.androidlauncher.data.model.entity.PageModel

@Dao
interface HomescreenPageDao {

    @Update
    fun updatePage(page: PageModel)

    @Delete
    fun deletePage(page: PageModel)

    @Query("SELECT * FROM PageModel ORDER BY pageNumber ASC")
    fun getAllPagesLiveData(): LiveData<List<PageModel>>

    @Query("SELECT * FROM PageModel ORDER BY pageNumber ASC")
    fun getAllPagesByPageNumberAsc() : MutableList<PageModel>

    /**
     * Returns all folders for given page with id of [pageId]
     */
    @Query("SELECT * FROM PageModel WHERE id = :pageId")
    fun getAllFolders(pageId: Int): LiveData<List<HomescreenPageFolders>>

    @Insert
    fun addPage(page: PageModel): Int

    @Query("SELECT * FROM PageModel WHERE id = :pageId")
    fun getPage(pageId: Int): PageModel?

    @Update
    fun updatePageList(pages: List<PageModel>)

}