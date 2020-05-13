package com.honzikv.androidlauncher.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.honzikv.androidlauncher.model.DockItemModel

@Dao
interface DockDao {

    @Query("SELECT * FROM DockItemModel")
    fun getAllItemsLiveData(): LiveData<List<DockItemModel>>

    @Query("SELECT * FROM DockItemModel ORDER BY id")
    suspend fun getAllItems(): MutableList<DockItemModel>

    @Insert
    suspend fun addItem(item: DockItemModel)

    @Insert
    suspend fun addItems(list: List<DockItemModel>)

    @Delete
    suspend fun removeItem(dockItemModel: DockItemModel)

    @Update
    suspend fun updateItems(items: List<DockItemModel>)


}