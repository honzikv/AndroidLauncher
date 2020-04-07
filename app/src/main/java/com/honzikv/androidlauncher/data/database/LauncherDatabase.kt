package com.honzikv.androidlauncher.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.honzikv.androidlauncher.data.database.dao.DockDao
import com.honzikv.androidlauncher.data.database.dao.FolderDao
import com.honzikv.androidlauncher.data.database.dao.PageDao
import com.honzikv.androidlauncher.data.database.dao.UserAppDao
import com.honzikv.androidlauncher.data.model.dto.*

/**
 * Room database to store user edited data about homescreen
 */
@Database(
    entities = [FolderDto::class, DockModel::class, FolderItemDto::class, DockItemModel::class,
        PageDto::class],
    version = 1
)
abstract class LauncherDatabase : RoomDatabase() {

    abstract fun folderDao(): FolderDao

    abstract fun dockDao(): DockDao

    abstract fun userAppDao(): UserAppDao

    abstract fun homescreenPageDao() : PageDao
}