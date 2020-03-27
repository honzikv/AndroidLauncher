package com.honzikv.androidlauncher.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.honzikv.androidlauncher.data.model.entity.FolderItemEntity

@Dao
interface UserAppDao {

    @Query("SELECT * FROM FolderEntity")
    fun getAll(): List<FolderItemEntity>
}
