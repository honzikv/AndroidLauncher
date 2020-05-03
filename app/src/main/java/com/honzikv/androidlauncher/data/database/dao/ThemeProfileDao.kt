package com.honzikv.androidlauncher.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.honzikv.androidlauncher.data.model.ThemeProfileModel

@Dao
interface ThemeProfileDao {

    @Query("SELECT * FROM ThemeProfileModel ORDER BY id ASC")
    fun getAllProfiles(): LiveData<List<ThemeProfileModel>>

    @Update
    fun updateProfile(vararg profile: ThemeProfileModel)

    @Update
    fun updateProfiles(list: List<ThemeProfileModel>)

    @Insert
    suspend fun addProfile(profile: ThemeProfileModel) : Long

    @Delete
    suspend fun delete(profile: ThemeProfileModel)

    @Insert
    suspend fun addProfiles(profiles: List<ThemeProfileModel>)

    @Transaction
    suspend fun deleteProfile(profile: ThemeProfileModel) {
        if (profile.isUserProfile) {
            deleteProfile(profile)
        }
    }
}