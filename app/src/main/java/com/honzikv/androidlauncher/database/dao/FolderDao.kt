package com.honzikv.androidlauncher.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.honzikv.androidlauncher.model.FolderItemModel
import com.honzikv.androidlauncher.model.FolderModel
import com.honzikv.androidlauncher.model.FolderWithItems

/**
 * Dao pro manipulaci se slozkou a jejimi aplikacemi
 */
@Dao
interface FolderDao {

    /**
     * Aktualizuje zastupce aplikace ve slozce definovaneho pomoci [folderItem]
     */
    @Update
    suspend fun updateFolderItem(folderItem: FolderItemModel)

    /**
     * Aktualizuje danou slozku (bez aktualizace aplikaci)
     */
    @Update
    suspend fun updateFolder(folder: FolderModel)

    /**
     * Prida slozku, ktera nema nastavenou stranku a vrati jeji id
     */
    @Insert
    suspend fun addFolderWithoutPage(folder: FolderModel): Long

    /**
     * Ziska slozku podle id [folderId]
     */
    @Query("SELECT * FROM FolderModel WHERE id = :folderId")
    suspend fun getFolder(folderId: Long): FolderModel

    /**
     * Smaze slozku s id [folderId]
     */
    @Query("DELETE FROM FolderModel WHERE :folderId = id")
    suspend fun deleteFolderWithId(folderId: Long)


    /**
     * Vrati slozku spolu s jejimi aplikacemi podle id slozky [folderId]. Data jsou zabalena do
     * LiveData a po zmene v databazi se automaticky aktualizuji. Musi bezet v transakci aby doslo
     * k validnimu nacteni
     */
    @Transaction
    @Query("SELECT * FROM FolderModel WHERE :folderId = id")
    fun getFolderWithItemsLiveData(folderId: Long): LiveData<FolderWithItems>

    /**
     * Vrati slozku spolu s jejimi aplikacemi podle id slozky [folderId].
     * Musi bezet v transakci, aby se data nacetla spravne. Data se po nacteni neaktualizuji
     */
    @Transaction
    @Query("SELECT * FROM FolderModel WHERE :folderId = id")
    suspend fun getFolderWithItems(folderId: Long): FolderWithItems

    /**
     * Vlozi seznam predmetu, ktere museji mit nastavene id slozky, ke ktere patri
     */
    @Insert
    suspend fun insertItemsWithFolderId(items: List<FolderItemModel>)

    /**
     * Smaze zastupce aplikace ve slozce s id [folderItemId] - nebere v potaz pozici, kterou musi
     * osetrit vyssi uroven aplikace
     */
    @Query("DELETE FROM FolderItemModel WHERE :folderItemId = id")
    suspend fun deleteFolderItem(folderItemId: Long)

    /**
     * Aktualizuje vsechny zastupce aplikaci ve slozce predane v parametru [items]
     */
    @Update
    suspend fun updateFolderItems(vararg items: FolderItemModel)

    /**
     * Vrati zastupce aplikace ve slozce s id [folderItemId]
     */
    @Query("SELECT * FROM FolderItemModel WHERE id = :folderItemId")
    suspend fun getFolderItem(folderItemId: Long): FolderItemModel

    /**
     * Aktualizuje seznam vsech slozek z [folderList]
     */
    @Update
    suspend fun updateFolderList(folderList: List<FolderModel>)

    /**
     * Aktualizuje seznam vsech aplikaci ve slozkach z [itemList]
     */
    @Update
    suspend fun updateFolderItemList(itemList: List<FolderItemModel>)


}