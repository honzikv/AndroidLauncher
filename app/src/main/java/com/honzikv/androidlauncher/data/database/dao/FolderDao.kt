package com.honzikv.androidlauncher.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.honzikv.androidlauncher.data.model.FolderModel
import com.honzikv.androidlauncher.data.model.FolderItemModel

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

}