package com.honzikv.androidlauncher.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.honzikv.androidlauncher.model.DockItemModel

/**
 * Dao objekt pro manipulaci s dokem
 */
@Dao
interface DockDao {

    /**
     * Vrati seznam vsech aplikaci v doku jako LiveData. Pri zmene se data automaticky aktualizuji
     */
    @Query("SELECT * FROM DockItemModel")
    fun getAllItemsLiveData(): LiveData<List<DockItemModel>>

    /**
     * Vrati seznam vsech predmetu v doku jako MutableList. Data se po nacteni dale neaktualizuji
     */
    @Query("SELECT * FROM DockItemModel ORDER BY id")
    suspend fun getAllItems(): MutableList<DockItemModel>

    /**
     * Prida vsechny predmety ze seznamu [list]. Nebere v potaz limit v doku, ktery musi vyresit
     * vyssi uroven aplikace
     */
    @Insert
    suspend fun addItems(list: List<DockItemModel>)

    /**
     * Odstrani predmet z databaze
     */
    @Delete
    suspend fun removeItem(dockItemModel: DockItemModel)

    /**
     * Aktualizuje seznam predmetu z [items] v databazi
     */
    @Update
    suspend fun updateItemList(items: List<DockItemModel>)


}