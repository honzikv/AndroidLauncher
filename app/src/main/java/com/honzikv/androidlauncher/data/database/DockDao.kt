package com.honzikv.androidlauncher.data.database

import androidx.room.Dao
import androidx.room.Query
import com.honzikv.androidlauncher.data.model.DockModel

@Dao
interface DockDao {

    @Query("Select * ")
    fun getDock() : DockModel?
}