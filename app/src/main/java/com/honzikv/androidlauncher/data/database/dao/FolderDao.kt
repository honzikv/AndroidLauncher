package com.honzikv.androidlauncher.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.honzikv.androidlauncher.data.model.FolderModel

@Dao
interface FolderDao {

    @Query("SELECT * from FolderModel")
    fun getFolders(): LiveData<List<FolderModel>>

}