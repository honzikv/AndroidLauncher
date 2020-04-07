package com.honzikv.androidlauncher.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.honzikv.androidlauncher.data.model.entity.DOCK_PRIMARY_KEY
import com.honzikv.androidlauncher.data.model.entity.DockDto
import com.honzikv.androidlauncher.data.model.entity.DockItemDto

@Dao
abstract class DockDao {

    @Query("SELECT * FROM DockDto WHERE primaryKey = $DOCK_PRIMARY_KEY")
    abstract fun getDock(): LiveData<DockDto?>


    @Query("SELECT * FROM DockItemDto")
    abstract fun getItems(): LiveData<List<DockItemDto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun createDock(dock: DockDto)
}