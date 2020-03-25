package com.honzikv.androidlauncher.data.database

import androidx.room.Dao
import androidx.room.Update

@Dao
interface FolderDao {

    @Update
    fun insertApp()
}