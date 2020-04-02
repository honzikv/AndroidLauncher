package com.honzikv.androidlauncher.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.honzikv.androidlauncher.data.database.dao.DockDao
import com.honzikv.androidlauncher.data.database.dao.FolderDao
import com.honzikv.androidlauncher.data.database.dao.HomescreenPageDao
import com.honzikv.androidlauncher.data.database.dao.UserAppDao
import com.honzikv.androidlauncher.data.model.entity.*

/**
 * Room database to store user edited data about homescreen
 */
@Database(
    entities = [FolderModel::class, DockModel::class, FolderItemModel::class, DockItemModel::class,
        PageModel::class],
    version = 1
)
abstract class LauncherDatabase : RoomDatabase() {

    abstract fun folderDao(): FolderDao

    abstract fun dockDao(): DockDao

    abstract fun userAppDao(): UserAppDao

    abstract fun homescreenPageDao() : HomescreenPageDao
}