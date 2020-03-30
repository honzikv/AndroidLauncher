package com.honzikv.androidlauncher.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.honzikv.androidlauncher.data.model.entity.HomescreenPageFolders
import com.honzikv.androidlauncher.data.model.entity.HomescreenPageModel

@Dao
interface HomescreenPageDao {

    @Update
    fun updatePage(page: HomescreenPageModel)

    @Delete
    fun deletePage(page: HomescreenPageModel)

    @Query("SELECT * FROM HomescreenPageModel ORDER BY pageNumber ASC")
    fun getAllPagesLiveData(): LiveData<List<HomescreenPageModel>>

    @Query("SELECT * FROM HomescreenPageModel ORDER BY pageNumber ASC")
    fun getAllPagesByPageNumberAsc() : MutableList<HomescreenPageModel>

    /**
     * Returns all folders for given page with id of [pageId]
     */
    @Query("SELECT * FROM HomescreenPageModel WHERE id = :pageId")
    fun getAllFolders(pageId: Int): LiveData<List<HomescreenPageFolders>>

    @Insert
    fun addPage(page: HomescreenPageModel): Int

    @Query("SELECT * FROM HomescreenPageModel WHERE id = :pageId")
    fun getPage(pageId: Int): HomescreenPageModel?

    @Update
    fun updatePageList(pages: List<HomescreenPageModel>)

}