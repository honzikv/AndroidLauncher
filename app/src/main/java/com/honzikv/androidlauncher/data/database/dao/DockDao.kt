package com.honzikv.androidlauncher.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.honzikv.androidlauncher.data.model.entity.DockItemModel

@Dao
abstract class DockDao {

    @Query("SELECT * FROM DockItemModel")
    abstract fun getItems(): LiveData<List<DockItemModel>>

}