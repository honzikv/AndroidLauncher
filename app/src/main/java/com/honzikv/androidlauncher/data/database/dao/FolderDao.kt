package com.honzikv.androidlauncher.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.honzikv.androidlauncher.data.model.entity.FolderEntity

@Dao
interface FolderDao {

    @Query("SELECT * from FolderEntity")
    fun getFolders(): LiveData<List<FolderEntity>>

}