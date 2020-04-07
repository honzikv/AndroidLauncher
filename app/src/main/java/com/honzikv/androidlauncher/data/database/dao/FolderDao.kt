package com.honzikv.androidlauncher.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.honzikv.androidlauncher.data.model.entity.FolderDto
import com.honzikv.androidlauncher.data.model.entity.FolderItemDto
import com.honzikv.androidlauncher.data.model.entity.PageWithFolders

@Dao
interface FolderDao {

    @Update
    fun updateFolderItem(folderItem: FolderItemDto)

    @Update
    suspend fun updateFolder(folder: FolderDto)

    @Insert
    suspend fun addFolder(folder: FolderDto): Long

    @Query("SELECT * FROM FolderDto WHERE id = :folderId")
    suspend fun getFolder(folderId: Long): FolderDto

    @Delete
    fun deleteFolder(folder: FolderDto)
}