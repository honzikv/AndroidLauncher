package com.honzikv.androidlauncher.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.honzikv.androidlauncher.model.ThemeProfileModel

@Dao
interface ThemeProfileDao {

    @Query("SELECT * FROM ThemeProfileModel ORDER BY id ASC")
    fun getAllProfilesLiveData(): LiveData<List<ThemeProfileModel>>

    @Insert
    suspend fun addProfile(profile: ThemeProfileModel) : Long
}