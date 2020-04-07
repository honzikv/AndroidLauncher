package com.honzikv.androidlauncher.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.honzikv.androidlauncher.data.model.dto.FolderItemDto

@Dao
interface UserAppDao {

    @Query("SELECT * FROM FolderDto")
    fun getAll(): LiveData<List<FolderItemDto>>
}
