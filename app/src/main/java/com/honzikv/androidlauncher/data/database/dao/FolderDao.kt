package com.honzikv.androidlauncher.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.honzikv.androidlauncher.data.model.entity.FolderItemModel
import com.honzikv.androidlauncher.data.model.entity.FolderModel
import com.honzikv.androidlauncher.data.model.entity.PageFolderList

@Dao
interface FolderDao {

    @Update
    fun updateFolderItem(folderItem: FolderItemModel)

    @Update
    fun updateFolder(folder: FolderModel)

    @Query("SELECT * FROM PageModel")
    fun getAllFolders(): LiveData<List<PageFolderList>>

    @Query("SELECT * FROM FolderModel WHERE pageId = :page ORDER BY position ASC")
    fun getFolders(page: Int): LiveData<List<FolderModel>>

    @Query("SELECT * FROM FolderModel WHERE pageId = :page ORDER BY position ASC")
    fun getFoldersOnPage(page: Int): List<FolderModel>

    @Insert
    fun addFolder(folder: FolderModel): Int

    @Query("SELECT * FROM FolderModel WHERE id = :folderId")
    suspend fun getFolder(folderId: Int): FolderModel

    @Query("SELECT * FROM FolderItemModel WHERE folderId = :folderId")
    fun getFolderItems(folderId: Int): LiveData<List<FolderItemModel>>

    @Delete
    fun deleteFolder(folder: FolderModel)
}