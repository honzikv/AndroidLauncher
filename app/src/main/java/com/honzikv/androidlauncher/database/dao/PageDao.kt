package com.honzikv.androidlauncher.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.honzikv.androidlauncher.model.PageModel
import com.honzikv.androidlauncher.model.PageWithFolders

/**
 * Dao pro manipulaci se strankou a jejimi slozkami
 */
@Dao
interface PageDao {

    /**
     * Nacte vsechny stranky se slozkami a pro slozky nacte vsechny jejich aplikace. Musi bezet
     * v transakci (@Transaction), jinak by data mohli byt nevalidni. Data vrati jako LiveData a
     * automaticky je aktualizuje pri zmene v databazi
     */
    @Transaction
    @Query("SELECT * FROM PageModel")
    fun getAllPagesWithFoldersLiveData(): LiveData<List<PageWithFolders>>

    /**
     * Aktualizuje stranku
     */
    @Update
    suspend fun updatePage(page: PageModel)

    /**
     * Smaze stranku. Nebere v potaz, ze stranka musi byt posledni - musi vyresit vyssi uroven aplikace
     */
    @Delete
    suspend fun deletePage(page: PageModel)

    /**
     * Ziska seznam vsech stranek serazeny podle cisla stranek. Ziskany seznam se neaktualizuje.
     */
    @Query("SELECT * FROM PageModel ORDER BY pageNumber ASC")
    suspend fun getAllPages(): MutableList<PageModel>

    /**
     * Prida stranku do databaze a vrati jeji id
     */
    @Insert
    suspend fun addPage(page: PageModel): Long

    /**
     * Aktualizace seznamu stranek
     */
    @Update
    suspend fun updatePageList(pages: List<PageModel>)

    /**
     * Ziska stranku s danym id podle [pageId]
     */
    @Query("SELECT * FROM PageModel WHERE id = :pageId")
    suspend fun getPage(pageId: Long): PageModel

    /**
     * Vrati nejvyssi cislo stranky
     */
    @Query("SELECT MAX(pageNumber) FROM PageModel")
    suspend fun getLastPageNumber(): Int?

    /**
     * Nacte jednu stranku s id [pageId] spolu se slozkami, ktere budou mit jejich aplikace.
     * Musi bezet v transakci. Data vrati jako LiveData a automaticky je bude aktualizovat
     */
    @Transaction
    @Query("SELECT * FROM PageModel WHERE id = :pageId")
    fun getPageWithFoldersLiveData(pageId: Long): LiveData<PageWithFolders>

    /**
     * Stejna funkce jako getPageWithFoldersLiveData akorat vrati primo PageWithFolders objekt.
     * Vraceny objekt se neaktualizuje.
     */
    @Transaction
    @Query("SELECT * FROM PageModel WHERE id = :pageId")
    suspend fun getPageWithFolders(pageId: Long): PageWithFolders

    /**
     * Vrati prvni stranku
     */
    @Query("SELECT * FROM PageModel WHERE pageNumber = 0")
    suspend fun getFirstPage(): PageModel
}