package com.honzikv.androidlauncher.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.honzikv.androidlauncher.data.model.entity.DOCK_PRIMARY_KEY
import com.honzikv.androidlauncher.data.model.entity.DockItemModel
import com.honzikv.androidlauncher.data.model.entity.DockModel
import kotlinx.coroutines.flow.Flow

@Dao
abstract class DockDao {

    @Query("SELECT * FROM DockModel WHERE primaryKey = $DOCK_PRIMARY_KEY")
    abstract fun getDock(): LiveData<DockModel?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertDock(dockModel: DockModel)

    @Query("SELECT * FROM DockItemModel")
    abstract fun getItems(): List<DockItemModel>

    @Insert
    abstract fun createDock(dock: DockModel)
}