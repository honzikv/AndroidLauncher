package com.honzikv.androidlauncher.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.honzikv.androidlauncher.model.ThemeProfileModel

/**
 * Dao pro ulozeni jednotlivych temat
 */
@Dao
interface ThemeProfileDao {

    /**
     * Ziska seznam vsech temat jako LiveData
     */
    @Query("SELECT * FROM ThemeProfileModel ORDER BY id ASC")
    fun getAllProfilesLiveData(): LiveData<List<ThemeProfileModel>>

    /**
     * Prida profil do databaze a vrati jeho id
     */
    @Insert
    suspend fun addProfile(profile: ThemeProfileModel) : Long
}