package com.honzikv.androidlauncher.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.honzikv.androidlauncher.data.model.DockModel

@Dao
interface DockDao {

    @Query("Select * ")
    fun getDock() : DockModel?
}