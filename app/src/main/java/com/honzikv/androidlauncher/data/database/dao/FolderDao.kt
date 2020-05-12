package com.honzikv.androidlauncher.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.honzikv.androidlauncher.data.model.FolderModel
import com.honzikv.androidlauncher.data.model.FolderItemModel
import com.honzikv.androidlauncher.data.model.FolderWithItems

@Dao
interface FolderDao {

    @Update
    fun updateFolderItem(folderItem: FolderItemModel)

    @Update
    suspend fun updateFolder(folder: FolderModel)

    @Insert
    suspend fun addFolder(folder: FolderModel): Long

    @Query("SELECT * FROM FolderModel WHERE id = :folderId")
    suspend fun getFolder(folderId: Long): FolderModel

    @Query("SELECT * FROM FolderModel WHERE id = :folderId")
    fun getFolderLiveData(folderId: Long): LiveData<FolderModel>

    @Delete
    suspend fun deleteFolder(folder: FolderModel)

    @Insert
    suspend fun addFolderItem(folderItem: FolderItemModel)

    @Query("DELETE FROM FolderModel WHERE :folderId = id")
    suspend fun deleteFolderWithId(folderId: Long)

    @Update
    suspend fun updateFolders(vararg folders: FolderModel)

    @Transaction
    @Query("SELECT * FROM FolderModel WHERE :folderId = id")
    suspend fun getFolderWithItems(folderId: Long): FolderWithItems

    @Insert
    suspend fun insertItemsWithFolderId(items: List<FolderItemModel>)

    @Transaction
    @Query("SELECT * FROM FolderModel WHERE :folderId = id")
    fun getFolderWithItemsLiveData(folderId: Long) : LiveData<FolderWithItems>

    @Query("DELETE FROM FolderItemModel WHERE :folderItemId = id")
    suspend fun deleteFolderItem(folderItemId: Long)

    @Update
    suspend fun updateFolderItems(vararg items: FolderItemModel)

}