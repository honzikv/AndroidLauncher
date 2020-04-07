package com.honzikv.androidlauncher.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.honzikv.androidlauncher.data.model.dto.DOCK_PRIMARY_KEY
import com.honzikv.androidlauncher.data.model.dto.DockItemModel
import com.honzikv.androidlauncher.data.model.dto.DockModel

@Dao
abstract class DockDao {

    @Query("SELECT * FROM DockDto WHERE primaryKey = $DOCK_PRIMARY_KEY")
    abstract fun getDock(): LiveData<DockModel?>


    @Query("SELECT * FROM DockItemDto")
    abstract fun getItems(): LiveData<List<DockItemModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun createDock(dock: DockModel)
}