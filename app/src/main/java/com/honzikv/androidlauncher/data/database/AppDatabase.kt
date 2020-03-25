package com.honzikv.androidlauncher.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.honzikv.androidlauncher.data.model.AppModel
import com.honzikv.androidlauncher.data.model.DockModel
import com.honzikv.androidlauncher.data.model.FolderModel

@Database(entities = [AppModel::class, FolderModel::class, DockModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao

    abstract fun folderDao(): FolderDao
}