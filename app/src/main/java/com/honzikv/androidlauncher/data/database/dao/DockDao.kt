package com.honzikv.androidlauncher.data.database.dao

import androidx.room.*
import com.honzikv.androidlauncher.data.model.entity.DOCK_PRIMARY_KEY
import com.honzikv.androidlauncher.data.model.entity.DockModel

@Dao
abstract class DockDao {

    @Query("SELECT * FROM DockModel WHERE primaryKey = $DOCK_PRIMARY_KEY")
    protected abstract fun getDock(): DockModel?

    @Transaction
    fun getInstance(): DockModel {
        return if (getDock() == null) {
            createDock()
        } else getDock() as DockModel
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertValue(dockModel: DockModel)

    @Transaction
    fun createDock(): DockModel {
        insertValue(DockModel())
        return getDock() as DockModel
    }

    @Transaction
    fun addApp() {
        val dock = getDock()
    }
}