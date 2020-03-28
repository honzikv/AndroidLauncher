package com.honzikv.androidlauncher.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.honzikv.androidlauncher.data.model.entity.FolderItemModel

@Dao
interface UserAppDao {

    @Query("SELECT * FROM FolderModel")
    fun getAll(): LiveData<List<FolderItemModel>>
}
