package com.honzikv.androidlauncher.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.honzikv.androidlauncher.database.dao.DockDao
import com.honzikv.androidlauncher.database.dao.FolderDao
import com.honzikv.androidlauncher.database.dao.PageDao
import com.honzikv.androidlauncher.database.dao.ThemeProfileDao
import com.honzikv.androidlauncher.model.*

/**
 * Room databaze se vsemi entitami pro persistenci dat v aplikaci.
 */
@Database(
    entities = [FolderModel::class, FolderItemModel::class, DockItemModel::class,
        PageModel::class, ThemeProfileModel::class],
    version = 1
)
abstract class LauncherDatabase : RoomDatabase() {

    abstract fun folderDao(): FolderDao

    abstract fun dockDao(): DockDao

    abstract fun homescreenPageDao(): PageDao

    abstract fun themeProfileDao(): ThemeProfileDao
}