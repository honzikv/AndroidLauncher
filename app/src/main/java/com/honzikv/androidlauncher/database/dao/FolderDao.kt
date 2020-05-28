package com.honzikv.androidlauncher.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.honzikv.androidlauncher.model.FolderModel
import com.honzikv.androidlauncher.model.FolderItemModel
import com.honzikv.androidlauncher.model.FolderWithItems

/**
 * Dao pro manipulaci se slozkou a jejimi aplikacemi
 */
@Dao
interface FolderDao {

    @Update
    suspend fun updateFolderItem(folderItem: FolderItemModel)

    @Update
    suspend fun updateFolder(folder: FolderModel)

    @Insert
    suspend fun addFolderWithoutPage(folder: FolderModel): Long

    @Query("SELECT * FROM FolderModel WHERE id = :folderId")
    suspend fun getFolder(folderId: Long): FolderModel

    @Query("DELETE FROM FolderModel WHERE :folderId = id")
    suspend fun deleteFolderWithId(folderId: Long)

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

    @Query("SELECT * FROM FolderItemModel WHERE id = :folderItemId")
    suspend fun getFolderItem(folderItemId: Long): FolderItemModel

    @Update
    suspend fun updateFolderList(folderList: List<FolderModel>)

    @Update
    suspend fun updateFolderItemList(itemList: List<FolderItemModel>)


}