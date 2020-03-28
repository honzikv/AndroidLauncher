package com.honzikv.androidlauncher.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Update
import com.honzikv.androidlauncher.data.model.entity.HomescreenPageModel

@Dao
interface HomescreenPageDao {

    @Update
    fun updatePage(page: HomescreenPageModel)

    @Delete
    fun deletePage(page: HomescreenPageModel)
}