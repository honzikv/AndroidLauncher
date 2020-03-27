package com.honzikv.androidlauncher.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.honzikv.androidlauncher.data.model.entity.DockEntity

@Dao
interface DockDao {

    @Query("SELECT * FROM DockEntity WHERE primaryKey = ${DockEntity().DOCK_PRIMARY_KEY}")
    fun getDock(): DockEntity
}