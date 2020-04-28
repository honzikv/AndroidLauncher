package com.honzikv.androidlauncher.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.honzikv.androidlauncher.data.model.entity.ThemeProfileDto

@Dao
interface ThemeProfileDao {

    @Query("SELECT * FROM ThemeProfileDto")
    fun getAllProfiles() : LiveData<List<ThemeProfileDto>>

    @Query("SELECT * FROM ThemeProfileDto WHERE isSelected")
    fun getSelectedProfile() : LiveData<ThemeProfileDto>

    @Insert
    fun addProfile(profile: ThemeProfileDto)

    @Delete
    fun deleteProfile(profile: ThemeProfileDto)
}