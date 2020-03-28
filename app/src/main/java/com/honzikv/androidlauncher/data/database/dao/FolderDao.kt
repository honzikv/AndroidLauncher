package com.honzikv.androidlauncher.data.database.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.honzikv.androidlauncher.data.model.entity.FolderModel

@Dao
interface FolderDao {

    @Query("SELECT * from FolderModel")
    fun getFolders(): MutableLiveData<List<FolderModel>>

    @Query("SELECT * from FolderModel WHERE page = :page ORDER BY position ASC")
    fun getFoldersOnPage(page: Int) : MutableLiveData<List<FolderModel>>

    @Insert
    fun addFolder(folder: FolderModel)

}