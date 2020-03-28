package com.honzikv.androidlauncher.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.honzikv.androidlauncher.data.model.entity.DOCK_PRIMARY_KEY
import com.honzikv.androidlauncher.data.model.entity.DockItemModel
import com.honzikv.androidlauncher.data.model.entity.DockModel

@Dao
abstract class DockDao {

    @Query("SELECT * FROM DockModel WHERE primaryKey = $DOCK_PRIMARY_KEY")
    protected abstract fun getDock(): LiveData<DockModel?>

    @Suppress("UNCHECKED_CAST")
    @Transaction
    fun getInstance(): LiveData<DockModel> {
        return if (getDock().value == null) {
            createDock()
        } else getDock() as LiveData<DockModel>
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertValue(dockModel: DockModel)

    @Suppress("UNCHECKED_CAST")
    @Transaction
    fun createDock(): LiveData<DockModel> {
        insertValue(DockModel())
        return getDock() as LiveData<DockModel>
    }

    @Transaction
    fun addApp() {
        val dock = getDock()
    }

    @Query("SELECT * FROM DockItemModel")
    abstract fun getItems(): List<DockItemModel>
}